package com.scnsoft.art.service.impl;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.dto.UploadFileDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.feignclient.FileFeignClient;
import com.scnsoft.art.repository.EntityFileRepository;
import com.scnsoft.art.service.FileService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.scnsoft.art.entity.EntityFile.Type.ORIGINAL;
import static com.scnsoft.art.entity.EntityFile.Type.THUMBNAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileFeignClient fileFeignClient;
    private final EntityFileRepository entityFileRepository;

    @Override
    public List<EntityFile> findAllByEntityId(UUID entityId) {
        return entityFileRepository.findAllByEntityId(entityId);
    }

    @Override
    public List<EntityFile> findAllThumbs(List<UUID> entityIds) {
        return entityFileRepository.findAllThumbs(entityIds);
    }


    @Override
    public EntityFile uploadFile(UploadEntityFileDto uploadEntityFileDto, EntityFile.Type type, UUID originalId) {
        EntityFile file;
        try {
            UploadFileDto uploadFileDto = UploadFileDto.builder()
                    .data(uploadEntityFileDto.getData())
                    .mimeType(uploadEntityFileDto.getMimeType())
                    .build();

            FileInfoDto response = fileFeignClient.uploadFile(uploadFileDto);

            file = EntityFile.builder()
                    .id(response.getId())
                    .entityId(uploadEntityFileDto.getEntityId())
                    .isPrimary(uploadEntityFileDto.getIsPrimary())
                    .originalId(originalId)
                    .type(type)
                    .build();
            if(THUMBNAIL.equals(file.getType())) {
                entityFileRepository.save(file);
            } else {
                save(file);
            }
        } catch (FeignException e) {
            log.error("Failed to save image: ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save image: " + e.getMessage());
        }

        return file;
    }

    @Override
    public EntityFile findById(UUID id) {
        return entityFileRepository.findById(id)
            .orElseThrow();
    }

    @Override
    public EntityFile save(EntityFile entityFile) {
        List<EntityFile> all = entityFileRepository.findAllByEntityId(entityFile.getEntityId());
        boolean updated = false;
        boolean currentPrimary = Boolean.TRUE.equals(entityFile.getIsPrimary());
        boolean primaryExists = currentPrimary;
        for(EntityFile file : all) {
            if(file.getId().equals(entityFile.getId()) || (
                    file.getOriginalId() != null &&
                    file.getOriginalId().equals(entityFile.getId()))
            ) {
                file.setIsPrimary(currentPrimary);
                entityFileRepository.save(file);
                updated = file.getId().equals(entityFile.getId());
            } else if(currentPrimary && Boolean.TRUE.equals(file.getIsPrimary())) {
                file.setIsPrimary(false);
                entityFileRepository.save(file);
            } else if(!currentPrimary && Boolean.TRUE.equals(file.getIsPrimary())) {
                primaryExists = true;
            }
        }
        if(!primaryExists) {
            entityFile.setIsPrimary(true);
        }
        if(!updated) {
            return entityFileRepository.save(entityFile);
        } else {
            return entityFile;
        }
    }

    @Override
    public void delete(UUID id) {
        List<EntityFile> thumbs = entityFileRepository.findAll(
                (r, q, cb) -> cb.equal(r.get(EntityFile.Fields.originalId), id));
        if(thumbs.size() > 0) {
            entityFileRepository.deleteAll(thumbs);
        }
        Optional<EntityFile> original = entityFileRepository.findById(id);
        if(original.isEmpty()) {
            return;
        }
        EntityFile file = original.get();
        entityFileRepository.delete(file);
        fileFeignClient.removeFileById(id);
        thumbs.stream()
                .map(EntityFile::getId)
                .forEach(fileFeignClient::removeFileById);


        if(Boolean.TRUE.equals(file.getIsPrimary())) {
            List<EntityFile> files = entityFileRepository.findAllByEntityId(file.getEntityId());
            files.sort(Comparator.comparing(EntityFile::getCreationDate));
            for(EntityFile existing : files) {
                if(ORIGINAL.equals(existing.getType())) {
                    existing.setIsPrimary(true);
                    save(existing);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteByArtId(UUID artId) {
        entityFileRepository.findAllByEntityId(artId)
            .forEach(file -> {
                fileFeignClient.removeFileById(file.getId());
                entityFileRepository.delete(file);
            });
        
    }
}

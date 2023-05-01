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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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
        EntityFile entityFileOriginal;
        try {
            UploadFileDto uploadFileDto = UploadFileDto.builder()
                    .data(uploadEntityFileDto.getData())
                    .mimeType(uploadEntityFileDto.getMimeType())
                    .build();

            FileInfoDto response = fileFeignClient.uploadFile(uploadFileDto);

            entityFileOriginal = EntityFile.builder()
                    .id(response.getId())
                    .entityId(uploadEntityFileDto.getEntityId())
                    .isPrimary(uploadEntityFileDto.getIsPrimary())
                    .originalId(originalId)
                    .type(type)
                    .build();


            entityFileRepository.save(entityFileOriginal);

        } catch (FeignException e) {
            log.error("Failed to save image: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save image: " + e.getMessage());
        }

        return entityFileOriginal;
    }

    @Override
    public EntityFile findById(UUID id) {
        return entityFileRepository.findById(id)
            .orElseThrow();
    }

    @Override
    public EntityFile save(EntityFile entityFile) {
        return entityFileRepository.save(entityFile);
    }

    @Override
    public void delete(UUID id) {
        List<EntityFile> thumbs = entityFileRepository.findAll(
                (r, q, cb) -> cb.equal(r.get(EntityFile.Fields.originalId), id));
        entityFileRepository.deleteAll(thumbs);
        entityFileRepository.deleteById(id);
        fileFeignClient.removeFileById(id);
        thumbs.stream()
                .map(EntityFile::getId)
                .forEach(fileFeignClient::removeFileById);
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

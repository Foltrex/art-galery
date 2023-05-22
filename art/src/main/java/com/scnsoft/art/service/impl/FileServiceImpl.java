package com.scnsoft.art.service.impl;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.repository.EntityFileRepository;
import com.scnsoft.art.service.FileService;
import com.scnsoft.art.service.FileServiceImplFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.scnsoft.art.entity.EntityFile.Type.ORIGINAL;
import static com.scnsoft.art.service.FileServiceImplFile.temp;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileServiceImplFile fileServiceImplFile;
    private final EntityFileRepository entityFileRepository;

    @Override
    public List<EntityFile> findAllThumbs(List<UUID> entityIds) {
        return entityFileRepository.findAllThumbs(entityIds);
    }
    @Override
    public List<EntityFile> findAllByEntityId(UUID entityId) {
        return entityFileRepository.findAllByEntityId(entityId);
    }

    @Override
    public FileInfo uploadTempFile(FileInfoDto uploadEntityFileDto) {
        uploadEntityFileDto.setDirectory(temp);
        return fileServiceImplFile.uploadFile(uploadEntityFileDto);
    }

    @Override
    public Optional<EntityFile> findById(UUID id) {
        return entityFileRepository.findById(id);
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
        Optional<EntityFile> original = entityFileRepository.findById(id);
        if(original.isEmpty()) {
            return;
        }
        EntityFile file = original.get();

        List<EntityFile> thumbs = entityFileRepository.findAll(
                (r, q, cb) -> cb.equal(r.get(EntityFile.Fields.originalId), id));
        if(thumbs.size() > 0) {
            entityFileRepository.deleteAll(thumbs);
        }
        entityFileRepository.delete(file);
        fileServiceImplFile.deleteById(id);
        thumbs.stream()
                .map(EntityFile::getId)
                .forEach(fileServiceImplFile::deleteById);


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
                fileServiceImplFile.deleteById(file.getId());
                entityFileRepository.delete(file);
            });
        
    }
}

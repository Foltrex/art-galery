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
    public List<EntityFile> uploadFile(UploadEntityFileDto uploadEntityFileDto) {
        EntityFile entityFileOriginal;
        EntityFile entityFileThumbnail;
        try {
            UploadFileDto uploadFileDto = UploadFileDto.builder()
                    .data(uploadEntityFileDto.getData())
                    .mimeType(uploadEntityFileDto.getMimeType())
                    .build();

            List<FileInfoDto> response = fileFeignClient.uploadFile(uploadFileDto);

            entityFileOriginal = EntityFile.builder()
                    .id(response.get(0).getId())
                    .entityId(uploadEntityFileDto.getEntityId())
                    .isPrimary(uploadEntityFileDto.getIsPrimary())
                    .originalId(null)
                    .type(EntityFile.Type.ORIGINAL)
                    .build();

            entityFileThumbnail = EntityFile.builder()
                    .id(response.get(1).getId())
                    .entityId(uploadEntityFileDto.getEntityId())
                    .originalId(entityFileOriginal.getId())
                    .isPrimary(uploadEntityFileDto.getIsPrimary())
                    .type(EntityFile.Type.THUMBNAIL)
                    .build();

            entityFileRepository.save(entityFileOriginal);
            entityFileRepository.save(entityFileThumbnail);

        } catch (FeignException e) {
            log.error("Failed to save image: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save image: " + e.getMessage());
        }

        return List.of(entityFileOriginal, entityFileThumbnail);
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
}

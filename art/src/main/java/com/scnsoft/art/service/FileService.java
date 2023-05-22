package com.scnsoft.art.service;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.FileInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    List<EntityFile> findAllThumbs(List<UUID> entityIds);

    List<EntityFile> findAllByEntityId(UUID entityId);

    FileInfo uploadTempFile(FileInfoDto uploadEntityFileDto);

    Optional<EntityFile> findById(UUID id);

    EntityFile save(EntityFile entityFile);

    void delete(UUID id);

    void deleteByArtId(UUID artId);
}

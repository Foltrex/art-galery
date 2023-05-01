package com.scnsoft.art.service;

import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.EntityFile;

import java.util.List;
import java.util.UUID;

public interface FileService {

    List<EntityFile> findAllByEntityId(UUID entityId);

    List<EntityFile> findAllThumbs(List<UUID> entityIds);

    EntityFile uploadFile(UploadEntityFileDto uploadEntityFileDto, EntityFile.Type type, UUID originalId);

    EntityFile findById(UUID id);

    EntityFile save(EntityFile entityFile);

    void delete(UUID id);

    void deleteByArtId(UUID artId);
}

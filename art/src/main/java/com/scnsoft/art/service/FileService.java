package com.scnsoft.art.service;

import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.EntityFile;

import java.util.List;
import java.util.UUID;

public interface FileService {

    List<EntityFile> findAllByEntityId(UUID entityId);

    List<EntityFile> uploadFile(UploadEntityFileDto uploadEntityFileDto);

    EntityFile findById(UUID id);

    EntityFile save(EntityFile entityFile);

    void delete(UUID id);

    void deleteByArtId(UUID artId);
}

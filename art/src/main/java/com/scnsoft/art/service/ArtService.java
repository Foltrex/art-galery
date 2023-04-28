package com.scnsoft.art.service;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.EntityFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtService {

    List<Art> findAll();

    Art save(Art art);

    Art findById(UUID id);

    void deleteById(UUID id);

    void deleteByAccountId(UUID accountId);

    Page<Art> findAll(Pageable pageable, ArtFilter artFilter);

    EntityFile uploadImage(UUID id, UploadEntityFileDto uploadEntityFileDto);
}

package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.entity.Art;

public interface ArtService {
    Page<Art> findAllByAccountId(UUID accountId, Pageable pageable);

    List<Art> findAll();

    Art save(Art art);

    Art findById(UUID id);
}

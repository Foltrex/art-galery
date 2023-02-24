package com.scnsoft.art.service;

import com.scnsoft.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtService {
    Page<Art> findAllByAccountIdAndName(UUID accountId, Pageable pageable, String name);

    List<Art> findAll();

    Art save(Art art);

    Art findById(UUID id);

    void deleteById(UUID id);
}

package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import com.scnsoft.art.entity.Art;

public interface ArtService {
    List<Art> findAllByAccountId(UUID accountId);

    List<Art> findAll();

    Art save(Art art);

    Art findById(UUID id);
}

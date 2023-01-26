package com.scnsoft.art.service;

import com.scnsoft.art.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ArtistService {

    Page<Artist> findAll(Pageable pageable);

    Artist findById(UUID id);

    boolean existWithAccountId(UUID accountId);

    Artist findByAccountId(UUID accountId);

    Artist save(Artist artist);

    Artist update(UUID id, Artist artist);

    void deleteById(UUID id);
}

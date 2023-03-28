package com.scnsoft.art.service;

import com.scnsoft.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArtService {
    Page<Art> findAllByAccountId(UUID accountId, Pageable pageable, String searchText, String searchFilter, String searchOption);

    Page<Art> findAllByArtistId(UUID artistId, Pageable pageable);

    List<Art> findAll();

    Art save(Art art);

    Art findById(UUID id);

    void deleteById(UUID id);

    Page<Art> findAll(Pageable pageable, String artistName, String cityName, String artNameAndDescription);
}

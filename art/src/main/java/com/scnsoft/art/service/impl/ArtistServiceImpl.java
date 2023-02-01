package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtistRepository;
import com.scnsoft.art.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public Page<Artist> findAll(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public Artist findById(UUID id) {
        return artistRepository
                .findById(id)
                .orElseThrow(() -> new ArtResourceNotFoundException("Artist not found by id"));
    }

    @Override
    public boolean existWithAccountId(UUID accountId) {
        return artistRepository
                .findByAccountId(accountId)
                .isPresent();
    }

    @Override
    public Artist findByAccountId(UUID accountId) {
        System.out.println(accountId);
        return artistRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new ArtResourceNotFoundException("Artist not found by accountId"));
    }

    @Override
    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public Artist update(UUID id, Artist artist) {
        artist.setId(id);
        return artistRepository.save(artist);
    }

    @Override
    public void deleteById(UUID id) {
        artistRepository.deleteById(id);
    }
}

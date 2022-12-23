package com.scnsoft.art.service;

import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findById(UUID id) {
        return artistRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist update(UUID id, Artist artist) {
        artist.setId(id);
        return artistRepository.save(artist);
    }

    public void deleteById(UUID id) {
        artistRepository.deleteById(id);
    }
}

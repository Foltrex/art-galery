package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.Artist;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl {

    private final ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findById(UUID id) {
        return artistRepository
                .findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found!"));
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

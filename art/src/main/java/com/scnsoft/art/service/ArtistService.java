package com.scnsoft.art.service;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.dto.mapper.impl.ArtistMapper;
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
    private final ArtistMapper artistMapper;

    public List<ArtistDto> findAll() {
        return artistRepository.findAll().stream()
                .map(artistMapper::mapToDto)
                .toList();
    }

    public ArtistDto findById(UUID id) {
        return artistRepository
                .findById(id)
                .map(artistMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public ArtistDto save(ArtistDto artistDto) {
        Artist artist = artistMapper.mapToEntity(artistDto);
        return artistMapper.mapToDto(artistRepository.save(artist));
    }

    public ArtistDto update(UUID id, ArtistDto artistDto) {
        Artist artist = artistMapper.mapToEntity(artistDto);
        artist.setId(id);
        return artistMapper.mapToDto(artistRepository.save(artist));
    }

    public void deleteById(UUID id) {
        artistRepository.deleteById(id);
    }
}

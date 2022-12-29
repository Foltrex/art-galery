package com.scnsoft.art.service;

import com.scnsoft.art.dto.UploadArtDto;
import com.scnsoft.art.dto.mapper.impl.UploadArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.FileFeignClient;
import com.scnsoft.art.repository.ArtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record ArtService(
        ArtRepository artRepository,
        UploadArtMapper artMapper,
        FileFeignClient fileFeignClient
) {
    public List<UploadArtDto> findAll() {
        return artRepository.findAll()
                .stream()
                .map(artMapper::mapToDto)
                .toList();
    }

    public UploadArtDto findById(UUID id) {
        return artRepository
                .findById(id)
                .map(artMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public UploadArtDto save(UploadArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        Art persistedArt = artRepository.save(art);
        UploadArtDto mappedPersistedArt = artMapper.mapToDto(persistedArt);
        return fileFeignClient.save(mappedPersistedArt);
    }

    public UploadArtDto update(UUID id, UploadArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        art.setId(id);
        Art updatedArt = artRepository.save(art);
        return artMapper.mapToDto(updatedArt);
    }

    public void deleteById(UUID id) {
        artRepository.deleteById(id);
    }
}

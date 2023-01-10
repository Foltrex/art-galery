package com.scnsoft.art.service;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.impl.ArtMapper;
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
        ArtMapper artMapper,
        FileFeignClient fileFeignClient
) {
    public List<ArtDto> findAll() {
        return artRepository.findAll()
                .stream()
                .map(artMapper::mapToDto)
                .toList();
    }

    public ArtDto findById(UUID id) {
        return artRepository
                .findById(id)
                .map(artMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public ArtDto save(ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        Art persistedArt = artRepository.save(art);
        artDto.setId(persistedArt.getId());
        return fileFeignClient.save(artDto);
    }

    public ArtDto update(UUID id, ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        art.setId(id);
        Art updatedArt = artRepository.save(art);
        return artMapper.mapToDto(updatedArt);
    }

    public void deleteById(UUID id) {
        artRepository.deleteById(id);
    }
}

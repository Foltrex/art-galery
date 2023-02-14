package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.mapper.impl.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.repository.ArtistRepository;
import com.scnsoft.art.service.ArtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public record ArtServiceImpl(
        ArtRepository artRepository,
        ArtistRepository artistRepository,
        ArtMapper artMapper
) implements ArtService {
    public List<Art> findAll() {
        return artRepository.findAll();
    }

    public Art findById(UUID id) {
        return artRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    // public ArtDto save(ArtDto artDto) {
    //     Art art = artMapper.mapToEntity(artDto);
    //     Art persistedArt = artRepository.save(art);
    //     artDto.setId(persistedArt.getId());
    //     return fileFeignClient.save(artDto);
    // }

    public Art save(Art art) {
        return artRepository.save(art);
    }

    public Art update(UUID id, Art art) {
        art.setId(id);
        return artRepository.save(art);
    }

    public void deleteById(UUID id) {
        artRepository.deleteById(id);
    }

    @Override
    public Page<Art> findAllByAccountIdAndName(UUID accountId, Pageable pageable, String artName) {
        Artist artist = artistRepository
            .findByAccountId(accountId)
            .orElseThrow(ArtResourceNotFoundException::new);
        
        return !Strings.isNullOrEmpty(artName) 
            ? artRepository.findAllByArtistAndName(artist, pageable, artName)
            : artRepository.findAllByArtist(artist, pageable);
    }
}

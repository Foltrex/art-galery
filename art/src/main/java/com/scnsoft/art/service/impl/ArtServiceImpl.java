package com.scnsoft.art.service.impl;

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
    public Page<Art> findAllByAccountId(UUID accountId, Pageable pageable) {
        Artist artist = artistRepository
                .findByAccountId(accountId)
                .orElseThrow(ArtResourceNotFoundException::new);

        return artRepository.findAllByArtist(artist, pageable);
    }
}

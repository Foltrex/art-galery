package com.scnsoft.art.facade;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.dto.mapper.impl.ArtistMapper;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.service.impl.ArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ArtistServiceFacade {

    private final ArtistServiceImpl artistService;
    private final ArtistMapper artistMapper;

    public Page<ArtistDto> findAll(Pageable pageable) {
        return artistMapper.mapPageToDto(artistService.findAll(pageable));
    }

    public ArtistDto findById(@PathVariable UUID id) {
        return artistMapper.mapToDto(artistService.findById(id));
    }

    public ArtistDto findByAccountId(@PathVariable UUID accountId) {
        return artistMapper.mapToDto(artistService.findByAccountId(accountId));
    }

    public ArtistDto save(@RequestBody ArtistDto artistDto) {
        Artist artist = artistMapper.mapToEntity(artistDto);
        return artistMapper.mapToDto(artistService.save(artist));
    }

    public ArtistDto updateById(UUID id, ArtistDto artistDto) {
        Artist artist = artistMapper.mapToEntity(artistDto);
        return artistMapper.mapToDto(artistService.update(id, artist));
    }

    public Void deleteById(@PathVariable UUID id) {
        artistService.deleteById(id);
        return null;
    }

    public Void deleteByAccountId(@PathVariable UUID accountId) {
        artistService.deleteByAccountId(accountId);
        return null;
    }

}

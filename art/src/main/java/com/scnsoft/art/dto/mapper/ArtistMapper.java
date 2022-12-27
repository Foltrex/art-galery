package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.entity.Artist;

@Component
public record ArtistMapper(
    CityMapper cityMapper
) {

    public ArtistDto mapToDto(Artist artist) {
        return ArtistDto.builder()
            .id(artist.getId())
            .firstname(artist.getFirstname())
            .lastname(artist.getLastname())
            .description(artist.getDescription())
            .accountId(artist.getAccountId())
            .cityDto(cityMapper.mapToDto(artist.getCity()))
            .build();
    }

    public Artist mapToEntity(ArtistDto artistDto) {
        return Artist.builder()
            .id(artistDto.getId())
            .firstname(artistDto.getFirstname())
            .lastname(artistDto.getLastname())
            .description(artistDto.getDescription())
            .accountId(artistDto.getAccountId())
            .city(cityMapper.mapToEntity(artistDto.getCityDto()))
            .build();
    }

}

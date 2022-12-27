package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistMapper {

    private final CityMapper cityMapper;

    public ArtistDto mapToDto(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .city(cityMapper.mapToDto(artist.getCity()))
                .firstname(artist.getFirstname())
                .lastname(artist.getLastname())
                .description(artist.getDescription())
                .accountId(artist.getAccountId())
                .build();
    }

    public Artist mapToEntity(ArtistDto artistDto) {
        return Artist.builder()
                .id(artistDto.getId())
                .city(cityMapper.mapToEntity(artistDto.getCity()))
                .firstname(artistDto.getFirstname())
                .lastname(artistDto.getLastname())
                .description(artistDto.getDescription())
                .accountId(artistDto.getAccountId())
                .build();
    }

}

package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.City;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArtistMapper implements Mapper<Artist, ArtistDto> {

    private final CityMapper cityMapper;

    @Override
    public ArtistDto mapToDto(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .city(mapCityToDto(artist.getCity()))
                .firstname(artist.getFirstname())
                .lastname(artist.getLastname())
                .description(artist.getDescription())
                .accountId(artist.getAccountId())
                .build();
    }

    @Override
    public Artist mapToEntity(ArtistDto artistDto) {
        return artistDto != null
                ? Artist.builder()
                .id(artistDto.getId())
                .city(mapCityDtoToEntity(artistDto.getCity()))
                .firstname(artistDto.getFirstname())
                .lastname(artistDto.getLastname())
                .description(artistDto.getDescription())
                .accountId(artistDto.getAccountId())
                .build()
                : null;
    }

    public Page<ArtistDto> mapPageToDto(final Page<Artist> artistPage) {
        return new PageImpl<>(artistPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), artistPage.getPageable(), artistPage.getTotalElements());
    }

    private CityDto mapCityToDto(City city) {
        return city != null ? cityMapper.mapToDto(city) : null;
    }

    private City mapCityDtoToEntity(CityDto cityDto) {
        return cityDto != null ? cityMapper.mapToEntity(cityDto) : null;
    }

}

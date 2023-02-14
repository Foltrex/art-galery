package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record ArtMapper(
        ArtistMapper artistMapper
) implements Mapper<Art, ArtDto> {

    @Override
    public ArtDto mapToDto(Art art) {
        return art != null
                ? ArtDto.builder()
                .id(art.getId())
                .name(art.getName())
                .description(art.getDescription())
                .artist(artistMapper.mapToDto(art.getArtist()))
                .build()
                : null;
    }

    @Override
    public Art mapToEntity(ArtDto artDto) {
        return artDto != null
                ? Art.builder()
                .id(artDto.getId())
                .name(artDto.getName())
                .description(artDto.getDescription())
                .artist(artistMapper.mapToEntity(artDto.getArtist()))
                .build()
                : null;
    }

    public Page<ArtDto> mapPageToDto(Page<Art> artPage) {
        List<ArtDto> artDtos = artPage
                .stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = artPage.getPageable();
        long totalElements = artPage.getTotalElements();
        return new PageImpl<>(artDtos, pageable, totalElements);
    }
}

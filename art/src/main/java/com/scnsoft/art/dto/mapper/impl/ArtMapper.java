package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Art;
import org.springframework.stereotype.Component;

@Component
public record ArtMapper(
        ArtistMapper artistMapper
) implements Mapper<Art, ArtDto> {

    @Override
    public ArtDto mapToDto(Art art) {
        return ArtDto.builder()
                .id(art.getId())
                .imageData(art.getFilename())
                .description(art.getDescription())
                .artistDto(artistMapper.mapToDto(art.getArtist()))
                .build();
    }

    @Override
    public Art mapToEntity(ArtDto artDto) {

        return Art.builder()
                .id(artDto.getId())
                .filename(artDto.getImageData())
                .description(artDto.getDescription())
                .artist(artistMapper.mapToEntity(artDto.getArtistDto()))
                .build();
    }
}

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
        return art != null
                ? ArtDto.builder()
                .id(art.getId())
                .description(art.getDescription())
                .fileId(art.getFileId())
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
                .fileId(artDto.getFileId())
                .artist(artistMapper.mapToEntity(artDto.getArtist()))
                .build()
                : null;
    }
}

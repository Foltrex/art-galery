package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.UploadArtDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Art;
import org.springframework.stereotype.Component;

@Component
public record UploadArtMapper(
        ArtistMapper artistMapper
) implements Mapper<Art, UploadArtDto> {

    @Override
    public UploadArtDto mapToDto(Art art) {
        return UploadArtDto.builder()
                .id(art.getId())
                .imageData(art.getFilename())
                .description(art.getDescription())
                .artistDto(artistMapper.mapToDto(art.getArtist()))
                .build();
    }

    @Override
    public Art mapToEntity(UploadArtDto artDto) {

        return Art.builder()
                .id(artDto.getId())
                .filename(artDto.getImageData())
                .description(artDto.getDescription())
                .artist(artistMapper.mapToEntity(artDto.getArtistDto()))
                .build();
    }
}

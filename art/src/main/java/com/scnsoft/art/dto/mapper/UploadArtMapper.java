package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.UploadArtDto;
import com.scnsoft.art.entity.Art;

@Component
public record UploadArtMapper(
    ArtistMapper artistMapper
) {
    public UploadArtDto mapToDto(Art art) {
        return UploadArtDto.builder()
            .id(art.getId())
            .imageData(art.getFilename())
            .description(art.getDescription())
            .artistDto(artistMapper.mapToDto(art.getArtist()))
            .build();
    }

    public Art mapToEntity(UploadArtDto artDto) {

        return Art.builder()
            .id(artDto.getId())
            .filename(artDto.getImageData())
            .description(artDto.getDescription())
            .artist(artistMapper.mapToEntity(artDto.getArtistDto()))
            .build();
    }
}

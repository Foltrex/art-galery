package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UploadArtDto {
    private UUID id;
    private String imageData;
    private String description;
    private ArtistDto artistDto;
}

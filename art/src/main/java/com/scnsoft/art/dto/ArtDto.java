package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class ArtDto {
    private UUID id;
    private String imageData;
    private String description;
    private ArtistDto artistDto;
}

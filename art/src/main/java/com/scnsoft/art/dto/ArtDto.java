package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtDto {
    private UUID id;
    private String name;
    private String imageData;
    private String description;
    private ArtistDto artist;
}

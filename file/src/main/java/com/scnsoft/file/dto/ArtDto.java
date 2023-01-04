package com.scnsoft.file.dto;

import java.util.UUID;

import lombok.Value;

@Value
public class ArtDto {
    private UUID id;
    private String name;
    private String imageData;
    private String description;
}

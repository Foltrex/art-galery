package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ArtistDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String description;
    private UUID accountId;
    private CityDto cityDto;
}

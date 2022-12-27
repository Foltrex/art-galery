package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ArtistDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String description;
    private UUID accountId;
    private CityDto city;
}

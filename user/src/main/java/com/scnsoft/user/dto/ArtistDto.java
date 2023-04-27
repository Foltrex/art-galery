package com.scnsoft.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String description;
    private UUID accountId;
    private CityDto city;
}

package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
    private AddressDto address;
}

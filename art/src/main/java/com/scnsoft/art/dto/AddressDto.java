package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record AddressDto(
    UUID id,
    CityDto cityDto,
    String streetName,
    int streetNumber
) {
}

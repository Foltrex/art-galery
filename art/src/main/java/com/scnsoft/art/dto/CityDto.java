package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CityDto(
    UUID id,
    String name,
    double latitude,
    double longitude
) {
}

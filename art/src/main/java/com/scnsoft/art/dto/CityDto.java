package com.scnsoft.art.dto;

import java.util.UUID;

public record CityDto(
    UUID id,
    String name,
    double latitude,
    double longitude
) {
}

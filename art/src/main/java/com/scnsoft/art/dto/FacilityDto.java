package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record FacilityDto(
    UUID id,
    String name,
    boolean isActive,
    UUID cityId
) {
}

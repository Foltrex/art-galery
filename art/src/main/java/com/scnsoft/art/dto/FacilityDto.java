package com.scnsoft.art.dto;

import java.util.UUID;

public record FacilityDto(
    UUID id,
    String name,
    boolean isActive,
    UUID cityId
) {
}

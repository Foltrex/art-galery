package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FacilityFilter {
    private UUID id;
    private UUID organizationId;
    private UUID cityId;
    private String facilityName;
    private Boolean isActive;
}

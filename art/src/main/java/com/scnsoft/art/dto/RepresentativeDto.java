package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RepresentativeDto {
    private UUID id;
    private OrganizationDto organization;
    private FacilityDto facility;
    private UUID accountId;
}

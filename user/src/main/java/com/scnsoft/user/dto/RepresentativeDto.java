package com.scnsoft.user.dto;

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

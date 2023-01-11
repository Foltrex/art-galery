package com.scnsoft.art.dto;

import com.scnsoft.art.entity.OrganizationRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RepresentativeDto {
    private UUID id;
    private OrganizationDto organization;
    private FacilityDto facility;
    private OrganizationRole organizationRole;
    private UUID accountId;
}

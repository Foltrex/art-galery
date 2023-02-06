package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RepresentativeDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private OrganizationDto organization;
    private FacilityDto facility;
    private OrganizationRoleDto organizationRole;
    private UUID accountId;
}

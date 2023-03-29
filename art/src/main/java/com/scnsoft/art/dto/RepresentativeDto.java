package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepresentativeDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private OrganizationDto organization;
    private FacilityDto facility;
    private String organizationRole;
    private UUID accountId;
}

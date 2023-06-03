package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Organization;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrganizationFilter {
    String name;
    Organization.Status status;
    Boolean withFacilities;
    UUID organizationId;
    UUID facilityId;
}

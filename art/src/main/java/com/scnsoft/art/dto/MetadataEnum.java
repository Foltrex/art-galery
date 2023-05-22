package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MetadataEnum {
    ACCOUNT_IMAGE("accountImage"),
    ORGANIZATION_ROLE("organizationRole"),
    ORGANIZATION_ID("organizationId"),
    FACILITY_ID("facilityId"),
    CITY_ID("cityId"),
    DESCRIPTION("description");

    private final String value;
}

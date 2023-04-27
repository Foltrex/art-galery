package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Builder
@Getter
@Setter
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {
    private UUID id;
    private String name;
    private Boolean isActive;
    private AddressDto address;

    private UUID organizationId;
    private String organizationName;
}
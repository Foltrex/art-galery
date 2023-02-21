package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {
    UUID id;
    String name;
    Boolean isActive;
    AddressDto address;
    OrganizationDto organization;
}

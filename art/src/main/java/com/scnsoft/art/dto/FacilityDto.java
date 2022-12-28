package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class FacilityDto {
    UUID id;
    String name;
    Boolean isActive;
    AddressDto addressDto;
}

package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FacilityDto {
    UUID id;
    String name;
    Boolean isActive;
    AddressDto addressDto;
}

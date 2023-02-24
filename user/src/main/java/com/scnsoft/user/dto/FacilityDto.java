package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FacilityDto {
    UUID id;
    String name;
    Boolean isActive;
    AddressDto addressDto;
}

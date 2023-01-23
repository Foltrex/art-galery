package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrganizationDto {
    private UUID id;
    private String name;
    private AddressDto address;
    private String status;
}

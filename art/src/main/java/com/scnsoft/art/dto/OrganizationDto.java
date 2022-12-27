package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Organization;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrganizationDto {
    private UUID id;
    private String name;
    private AddressDto address;
    private Organization.Status status;
}

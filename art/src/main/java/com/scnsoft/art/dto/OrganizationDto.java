package com.scnsoft.art.dto;

import java.util.UUID;

import com.scnsoft.art.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    private UUID id;
    private String name;
    private AddressDto address;
    private Organization.Status status;
}

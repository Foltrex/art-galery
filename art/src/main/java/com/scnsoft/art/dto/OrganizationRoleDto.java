package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationRoleDto {
    private Long id;
    private String name;
}

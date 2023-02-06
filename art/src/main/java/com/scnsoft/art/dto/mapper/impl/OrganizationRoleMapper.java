package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.entity.OrganizationRole.RoleType;
import org.springframework.stereotype.Component;

@Component
public class OrganizationRoleMapper implements Mapper<OrganizationRole, OrganizationRoleDto> {

    @Override
    public OrganizationRoleDto mapToDto(OrganizationRole organizationRole) {
        return OrganizationRoleDto.builder()
                .id(organizationRole.getId())
                .name(organizationRole.getName().name())
                .build();
    }

    @Override
    public OrganizationRole mapToEntity(OrganizationRoleDto organizationRoleDto) {
        return OrganizationRole.builder()
                .id(organizationRoleDto.getId())
                .name(RoleType.valueOf(organizationRoleDto.getName()))
                .build();
    }

}

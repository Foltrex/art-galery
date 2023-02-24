package com.scnsoft.art.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.entity.OrganizationRole;

@Mapper(componentModel = "spring")
public interface OrganizationRoleMapper {

    OrganizationRoleDto mapToDto(OrganizationRole organizationRole);

    OrganizationRole mapToEntity(OrganizationRoleDto organizationRoleDto);
}

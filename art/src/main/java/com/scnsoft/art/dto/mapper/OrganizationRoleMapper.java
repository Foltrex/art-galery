package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.entity.OrganizationRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationRoleMapper {

    OrganizationRoleDto mapToDto(OrganizationRole organizationRole);

    OrganizationRole mapToEntity(OrganizationRoleDto organizationRoleDto);
}

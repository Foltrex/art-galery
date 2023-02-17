package com.scnsoft.art.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.entity.OrganizationRole.RoleType;

@Mapper(componentModel = "spring")
public interface OrganizationRoleMapper {

    // @Override
    // public OrganizationRoleDto mapToDto(OrganizationRole organizationRole) {
    //     return OrganizationRoleDto.builder()
    //             .id(organizationRole.getId())
    //             .name(organizationRole.getName().name())
    //             .build();
    // }
    OrganizationRoleDto mapToDto(OrganizationRole organizationRole);

    OrganizationRole mapToEntity(OrganizationRoleDto organizationRoleDto);

    // @Override
    // public OrganizationRole mapToEntity(OrganizationRoleDto organizationRoleDto) {
    //     return OrganizationRole.builder()
    //             .id(organizationRoleDto.getId())
    //             .name(RoleType.valueOf(organizationRoleDto.getName()))
    //             .build();
    // }

}

package com.scnsoft.art.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.dto.mapper.impl.OrganizationMapper;
import com.scnsoft.art.dto.mapper.impl.OrganizationRoleMapper;
import com.scnsoft.art.service.OrganizationRoleService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationRoleServiceFacade {

    private final OrganizationRoleService organizationRoleService;
    private final OrganizationRoleMapper organizationRoleMapper;

    public List<OrganizationRoleDto> findAll() {
        return organizationRoleService.findAll()
            .stream()
            .map(organizationRoleMapper::mapToDto)
            .toList();
    }

}

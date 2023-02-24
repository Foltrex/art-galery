package com.scnsoft.art.facade;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.dto.mapper.OrganizationRoleMapper;
import com.scnsoft.art.service.OrganizationRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

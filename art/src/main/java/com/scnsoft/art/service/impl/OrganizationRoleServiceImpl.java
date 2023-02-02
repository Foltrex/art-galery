package com.scnsoft.art.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.repository.OrganizationRoleRepository;
import com.scnsoft.art.service.OrganizationRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationRoleServiceImpl implements OrganizationRoleService {

    private final OrganizationRoleRepository organizationRoleRepository;

    @Override
    public List<OrganizationRole> findAll() {
        return organizationRoleRepository.findAll();
    }
    
}

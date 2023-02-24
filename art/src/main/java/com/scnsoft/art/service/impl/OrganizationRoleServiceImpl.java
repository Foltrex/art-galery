package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.repository.OrganizationRoleRepository;
import com.scnsoft.art.service.OrganizationRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationRoleServiceImpl implements OrganizationRoleService {

    private final OrganizationRoleRepository organizationRoleRepository;

    @Override
    public List<OrganizationRole> findAll() {
        return organizationRoleRepository.findAll();
    }

}

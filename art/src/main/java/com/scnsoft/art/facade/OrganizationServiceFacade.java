package com.scnsoft.art.facade;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.mapper.OrganizationMapper;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.service.impl.OrganizationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizationServiceFacade {

    private final OrganizationServiceImpl organizationService;
    private final OrganizationMapper organizationMapper;


    public Page<OrganizationDto> findAll(Pageable pageable, String name, String status) {
        return organizationMapper.mapPageToDto(organizationService.findAll(pageable, name, status));
    }

    public OrganizationDto findById(UUID id) {
        return organizationMapper.mapToDto(organizationService.findById(id));
    }

    public OrganizationDto findByAccountId(UUID accountId) {
        return organizationMapper.mapToDto(organizationService.findByAccountId(accountId));
    }

    public OrganizationDto save(OrganizationDto organizationDto) {
        Organization organization = organizationMapper.mapToEntity(organizationDto);
        return organizationMapper.mapToDto(organizationService.save(organization));
    }

    public OrganizationDto updateById(UUID id, OrganizationDto organizationDto) {
        Organization organization = organizationMapper.mapToEntity(organizationDto);
        return organizationMapper.mapToDto(organizationService.update(id, organization));
    }

    public Void deleteById(@PathVariable UUID id) {
        organizationService.deleteById(id);
        return null;
    }
}

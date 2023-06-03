package com.scnsoft.art.facade;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.OrganizationFilter;
import com.scnsoft.art.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.mapper.OrganizationMapper;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.service.impl.OrganizationServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationServiceFacade {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;


    public List<OrganizationDto> findAll() {
        return organizationService.findAll()
            .stream()
            .map(organizationMapper::mapToLiteDto)
            .toList();
    }

    public Page<OrganizationDto> findAll(Pageable pageable, OrganizationFilter filter, Date inactiveDate) {
        return organizationMapper.mapPageToDto(organizationService.findAll(pageable, filter, inactiveDate));
    }

    public OrganizationDto findById(UUID id) {
        return organizationMapper.mapToDto(organizationService.findById(id));
    }

    public OrganizationDto findByName(String name) {
        return organizationMapper.mapToDto(organizationService.findByName(name));
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

    public void deleteById(@PathVariable UUID id) {
        organizationService.deleteById(id);
    }
}

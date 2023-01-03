package com.scnsoft.art.service;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.mapper.impl.OrganizationMapper;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record OrganizationService(OrganizationRepository organizationRepository,
                                  OrganizationMapper organizationMapper) {

    public List<OrganizationDto> findAll() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::mapToDto)
                .toList();
    }

    public OrganizationDto findById(UUID id) {
        return organizationRepository
                .findById(id)
                .map(organizationMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

//    public Organization findByAccountId(UUID accountId) {
//        return organizationRepository
//                .findByAccountId(accountId)
//                .orElseThrow(ArtResourceNotFoundException::new);
//    }

    public OrganizationDto save(OrganizationDto OrganizationDto) {
        Organization organization = organizationMapper.mapToEntity(OrganizationDto);
        return organizationMapper.mapToDto(organizationRepository.save(organization));
    }

    public OrganizationDto update(UUID id, OrganizationDto OrganizationDto) {
        Organization organization = organizationMapper.mapToEntity(OrganizationDto);
        organization.setId(id);
        return organizationMapper.mapToDto(organizationRepository.save(organization));
    }

    public void deleteById(UUID id) {
        organizationRepository.deleteById(id);
    }
}

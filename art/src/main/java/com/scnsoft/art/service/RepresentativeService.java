package com.scnsoft.art.service;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.dto.mapper.impl.RepresentativeMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.repository.OrganizationRepository;
import com.scnsoft.art.repository.OrganizationRoleRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record RepresentativeService(RepresentativeRepository representativeRepository,
                                    OrganizationRepository organizationRepository,
                                    FacilityRepository facilityRepository,
                                    OrganizationRoleRepository organizationRoleRepository,
                                    RepresentativeMapper representativeMapper) {

    public List<RepresentativeDto> findAll() {
        return representativeRepository.findAll().stream()
                .map(representativeMapper::mapToDto)
                .toList();
    }

    public RepresentativeDto findById(UUID id) {
        return representativeRepository
                .findById(id)
                .map(representativeMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public RepresentativeDto save(RepresentativeDto representativeDto) {
        Representative representative = representativeMapper.mapToEntity(representativeDto);

        if (representative.getOrganization() == null && representative.getFacility() == null) {
            Organization organization = Organization.builder()
                    .build();

            organization = organizationRepository.save(organization);

            Facility facility = Facility.builder()
                    .organization(organization)
                    .build();

            facility = facilityRepository.save(facility);

            representative.setOrganization(organization);
            representative.setFacility(facility);
            representative.setOrganizationRole(organizationRoleRepository
                    .findByName(OrganizationRole.RoleType.CREATOR)
                    .orElseThrow(() -> new ArtResourceNotFoundException("OrganizationRole not found!")));
        } else {
            representative.setOrganizationRole(organizationRoleRepository
                    .findByName(OrganizationRole.RoleType.MEMBER)
                    .orElseThrow(() -> new ArtResourceNotFoundException("OrganizationRole not found!")));
        }

        log.info(representative.toString());
        return representativeMapper.mapToDto(representativeRepository.save(representative));
    }

    public RepresentativeDto update(UUID id, RepresentativeDto representativeDto) {
        Representative representative = representativeMapper.mapToEntity(representativeDto);
        representative.setId(id);
        return representativeMapper.mapToDto(representativeRepository.save(representative));
    }

    public void deleteById(UUID id) {
        representativeRepository.deleteById(id);
    }
}

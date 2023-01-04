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
import com.scnsoft.art.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Representative findByAccountId(UUID accountId) {
        return representativeRepository
                .findByAccountId(accountId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public RepresentativeDto save(RepresentativeDto representativeDto) {
        Representative representative = representativeMapper.mapToEntity(representativeDto);

        if (representative.getOrganization() == null && representative.getFacility() == null) {
            if (representativeRepository.findByAccountId(SecurityUtil.getCurrentAccountId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Representative is already exists!");
            }

            Organization organization = organizationRepository.save(Organization.builder().build());
            Facility facility = facilityRepository.save(Facility.builder().organization(organization).build());

            representative.setOrganization(organization);
            representative.setFacility(facility);
            representative.setOrganizationRole(getOrganizationRoleByName(OrganizationRole.RoleType.CREATOR));
        } else {
            Representative existedRepresentative = findByAccountId(SecurityUtil.getCurrentAccountId());

            if (representativeRepository.findByAccountId(representative.getAccountId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Representative is already exists!");
            }

            Organization organization = getOrganizationById(representative.getOrganization().getId());
            Facility facility = getFacilityById(representative.getFacility().getId());

            if (!organization.getId().equals(existedRepresentative.getOrganization().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid organization!");
            }

            if (!facility.getOrganization().getId().equals(organization.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid facility!");
            }

            representative.setOrganization(organization);
            representative.setFacility(facility);
            representative.setOrganizationRole(getOrganizationRoleByName(OrganizationRole.RoleType.MEMBER));
        }

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

    private OrganizationRole getOrganizationRoleByName(OrganizationRole.RoleType name) {
        return organizationRoleRepository
                .findByName(name)
                .orElseThrow(() -> new ArtResourceNotFoundException("OrganizationRole not found by name: " + name));
    }

    private Organization getOrganizationById(UUID id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new ArtResourceNotFoundException("Organization not found by id: " + id));
    }

    private Facility getFacilityById(UUID id) {
        return facilityRepository
                .findById(id)
                .orElseThrow(() -> new ArtResourceNotFoundException("Facility not found by id: " + id));
    }
}

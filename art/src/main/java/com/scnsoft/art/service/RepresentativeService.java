package com.scnsoft.art.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
public record RepresentativeService(RepresentativeRepository representativeRepository,
                                    OrganizationRepository organizationRepository,
                                    FacilityRepository facilityRepository,
                                    OrganizationRoleRepository organizationRoleRepository,
                                    RepresentativeMapper representativeMapper) {

    public Page<Representative> findAll(Pageable pageable) {
        return representativeRepository.findAll(pageable);
    }

    public Representative findById(UUID id) {
        return representativeRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Representative findByAccountId(UUID accountId) {
        return representativeRepository
                .findByAccountId(accountId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Representative save(Representative representative) {
        if (representativeRepository.findByAccountId(representative.getAccountId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Representative is already exists!");
        }

        Organization organization;
        Facility facility = null;

        if (representative.getOrganization() == null && representative.getFacility() == null) {
            organization = organizationRepository.save(Organization.builder().build());
            facility = facilityRepository.save(Facility.builder().organization(organization).build());
            representative.setOrganizationRole(getOrganizationRoleByName(OrganizationRole.RoleType.CREATOR));
        } else {
            Representative currentAuthorizedRepresentative = findByAccountId(SecurityUtil.getCurrentAccountId());
            organization = getOrganizationById(representative.getOrganization().getId());

            if (!organization.getId().equals(currentAuthorizedRepresentative.getOrganization().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid organization!");
            }

            if (representative.getFacility().getId() != null) {
                facility = getFacilityById(representative.getFacility().getId());
                if (!facility.getOrganization().getId().equals(organization.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid facility!");
                }
            }
            representative.setOrganizationRole(getOrganizationRoleByName(OrganizationRole.RoleType.MEMBER));
        }

        representative.setOrganization(organization);
        representative.setFacility(facility);

        return representativeRepository.save(representative);
    }

    public Representative update(UUID id, Representative representative) {
        Representative existedRepresentative = findById(id);
        representative.setId(id);
        representative.setOrganization(existedRepresentative.getOrganization());

        return representativeRepository.save(representative);
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

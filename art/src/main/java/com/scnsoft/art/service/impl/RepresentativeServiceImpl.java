package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.OrganizationRoleRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepresentativeServiceImpl {

    private final RepresentativeRepository representativeRepository;
    private final OrganizationServiceImpl organizationService;
    private final FacilityService facilityService;
    private final OrganizationRoleRepository organizationRoleRepository;

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
            organization = organizationService.save(Organization.builder().build());
            facility = facilityService.save(Facility.builder().organization(organization).build());
            representative.setOrganizationRole(getOrganizationRoleByName(OrganizationRole.RoleType.CREATOR));
        } else {
            Representative currentAuthorizedRepresentative = findByAccountId(SecurityUtil.getCurrentAccountId());
            organization = organizationService.findById(representative.getOrganization().getId());

            if (!organization.getId().equals(currentAuthorizedRepresentative.getOrganization().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid organization!");
            }

            if (representative.getFacility().getId() != null) {
                facility = facilityService.findById(representative.getFacility().getId());
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

}

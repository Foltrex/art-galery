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
import com.scnsoft.art.service.RepresentativeService;
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
public class RepresentativeServiceImpl implements RepresentativeService {

    private final RepresentativeRepository representativeRepository;
    private final OrganizationServiceImpl organizationService;
    private final FacilityService facilityService;
    private final OrganizationRoleRepository organizationRoleRepository;

    @Override
    public Page<Representative> findAll(Pageable pageable) {
        return representativeRepository.findAll(pageable);
    }

    @Override
    public Representative findById(UUID id) {
        return representativeRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    @Override
    public Representative findByAccountId(UUID accountId) {
        return representativeRepository
                .findByAccountId(accountId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    @Override
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

    @Override
    public Representative update(UUID id, Representative representative) {
        Representative existedRepresentative = findById(id);
        representative.setId(id);
        representative.setAccountId(existedRepresentative.getAccountId());
        representative.setOrganization(existedRepresentative.getOrganization());

        return representativeRepository.save(representative);
    }

    @Override
    public void deleteById(UUID id) {
        representativeRepository.deleteById(id);
    }

    @Override
    public void deleteByAccountId(UUID accountId) {
        Representative representative = findByAccountId(accountId);
        if (representative.getOrganizationRole().getName().equals(OrganizationRole.RoleType.CREATOR)) {
            Organization organization = organizationService.findByAccountId(accountId);
            organization.setStatus(Organization.Status.INACTIVE);
        }
        deleteById(representative.getId());
    }

    private OrganizationRole getOrganizationRoleByName(OrganizationRole.RoleType name) {
        return organizationRoleRepository
                .findByName(name)
                .orElseThrow(() -> new ArtResourceNotFoundException("OrganizationRole not found by name: " + name));
    }

    public Page<Representative> findAllByAccountId(UUID accountId, Pageable pageable) {
        Representative representative = representativeRepository
            .findByAccountId(accountId)
            .orElseThrow(IllegalArgumentException::new);
        
        return representativeRepository.findAllByOrganization(representative.getOrganization(), pageable);
    }

}

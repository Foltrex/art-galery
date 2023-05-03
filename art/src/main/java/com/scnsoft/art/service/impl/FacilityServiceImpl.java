package com.scnsoft.art.service.impl;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.repository.specification.FacilitySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.MetaData;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.feignclient.MetadataFeignClient;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FacilityService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private static final String ORGANIZATION_ROLE_KEY = "organizationRole";
    private static final String FACILITY_KEY = "facilityId";
    private final List<String> administrativeOrganizationRoles = List.of(
        "CREATOR", "MODERATOR"
    );

    private final FacilityRepository facilityRepository;
    private final OrganizationServiceImpl organizationService;
    private final AccountFeignClient accountFeignClient;
    private final MetadataFeignClient metadataFeignClient;

    @Override
    public Facility findByAccountId(UUID accountId) {
        // Representative representative = representativeRepository
        //     .findByAccountId(accountId)
        //     .orElseThrow(ArtResourceNotFoundException::new);

        // return representative.getFacility();
        // TODO: fix latter
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Facility> findAll(Pageable pageable) {
        return facilityRepository.findAll(pageable);
    }

    

    @Override
    public Page<Facility> findAllByOrganizationId(UUID id, Pageable pageable) {
        Organization organization = organizationService.findById(id);
        return facilityRepository.findAllByOrganization(organization, pageable);
    }

    @Override
    public Facility findById(UUID id) {
        return facilityRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facility with id " + id + " not found"));
    }

    @Override
    public Facility save(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Override
    public Facility updateById(UUID id, Facility facility) {
        facility.setId(id);
        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        AccountDto accountDto = accountFeignClient.findById(currentAccountId);
        Organization organization;
        if(accountDto.getAccountType() == AccountType.SYSTEM) {
            organization = organizationService.findById(facility.getOrganization().getId());
        } else {
            Facility oldFacility = findById(id);
            organization = oldFacility.getOrganization();
        }
        facility.setOrganization(organization);
        return facilityRepository.save(facility);
    }

    @Override
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        facilityRepository.deleteById(id);
    }

    @Override
    public Page<Facility> findAllByAccountId(UUID accountId, Pageable pageable) {
        throw new UnsupportedOperationException();
        // AccountDto accountDto = accountFeignClient.findById(accountId);

        // // Organization organization = organizationRepository.findBy;
        // return facilityRepository.findAllByOrganization(organization, pageable);
    }

    @Override
    public List<Facility> findAllByAccountId(UUID accountId) {
        // Representative representative = representativeRepository.findByAccountId(accountId)
        //         .orElseThrow(IllegalArgumentException::new);

        // Organization organization = representative.getOrganization();
        // return facilityRepository.findAllByOrganization(organization);
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Facility> findAll(
        Pageable pageable, 
        FacilityFilter filter
    ) {
        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        AccountDto accountDto = accountFeignClient.findById(currentAccountId);

        switch (accountDto.getAccountType()) {
            case SYSTEM:
            case ARTIST: {
                Specification<Facility> generalSpecification = new FacilitySpecification(filter);
                return facilityRepository.findAll(generalSpecification, pageable);
            }
            case REPRESENTATIVE: {
                MetaData organizationRoleMetadata = metadataFeignClient
                    .findByKeyAndAccountId(currentAccountId, ORGANIZATION_ROLE_KEY);
                String organizationRole = organizationRoleMetadata.getValue();
                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    Specification<Facility> generalSpecification = new FacilitySpecification(filter);
                    return facilityRepository.findAll(generalSpecification, pageable);
                } else {
                    MetaData facilityMetadata = metadataFeignClient
                        .findByKeyAndAccountId(currentAccountId, FACILITY_KEY);
                    UUID facilityId = UUID.fromString(facilityMetadata.getValue());
                    filter.setId(facilityId);
                    return facilityRepository.findAll(new FacilitySpecification(filter), pageable);
                }
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public List<Facility> findAll(FacilityFilter filter) {
        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        AccountDto accountDto = accountFeignClient.findById(currentAccountId);


        switch (accountDto.getAccountType()) {
            case SYSTEM:
            case ARTIST: {
                return facilityRepository.findAll(new FacilitySpecification(filter));
            }
            case REPRESENTATIVE: {
                MetaData organizationRoleMetadata = metadataFeignClient
                    .findByKeyAndAccountId(currentAccountId, ORGANIZATION_ROLE_KEY);
                String organizationRole = organizationRoleMetadata.getValue();
                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    return facilityRepository.findAll(new FacilitySpecification(filter));
                } else {
                    MetaData facilityMetadata = metadataFeignClient
                        .findByKeyAndAccountId(currentAccountId, FACILITY_KEY);
                    UUID facilityId = UUID.fromString(facilityMetadata.getValue());
                    filter.setId(facilityId);

                    return facilityRepository.findAll(new FacilitySpecification(filter));
                }
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public List<Facility> findAllByOrganizationId(UUID organizationId) {
        return facilityRepository.findByOrganizationId(organizationId);
    }
}

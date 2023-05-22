package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.repository.specification.FacilitySpecification;
import com.scnsoft.art.service.user.AccountServiceImpl;
import com.scnsoft.art.service.user.MetadataServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FacilityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private final List<String> administrativeOrganizationRoles = List.of(
        "CREATOR", "MODERATOR"
    );

    private final FacilityRepository facilityRepository;
    private final OrganizationServiceImpl organizationService;
    private final AccountServiceImpl accountService;
    private final MetadataServiceImpl metadataService;
    private final ImageService imageService;

    @Value("${app.props.facility.image_width}")
    private Integer facilityImageWidth;

    @Value("${app.props.facility.image_height}")
    private Integer facilityImageHeight;


    @Override
    public Page<Facility> findAll(Pageable pageable) {
        return facilityRepository.findAll(pageable);
    }

    @Override
    public Optional<Facility> findById(UUID id) {
        return facilityRepository.findById(id);
    }

    @Override
    public Facility save(Facility facility, List<EntityFile> images) {
        var id = facility.getId();
        if(id == null) {
            facility = facilityRepository.save(facility);
            id = facility.getId();
        }
        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        Account accountDto = accountService.findById(currentAccountId);

        Organization organization;
        if(accountDto.getAccountType() == AccountType.SYSTEM) {
            organization = organizationService.findById(facility.getOrganization().getId());
        } else {
            Metadata orgId = metadataService.findByKeyAndAccountId(
                    MetadataEnum.ORGANIZATION_ID.getValue(),
                    currentAccountId);
            organization = organizationService.findById(UUID.fromString(orgId.getValue()));
        }
        facility.setOrganization(organization);


        imageService.processImages(
                facility.getId(),
                images,
                "/facility/" + facility.getId() + "/image/",
                facilityImageWidth,
                facilityImageHeight,
                null, null);

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
    public Page<Facility> findAll(
        Pageable pageable, 
        FacilityFilter filter
    ) {
        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        Account accountDto = accountService.findById(currentAccountId);

        switch (accountDto.getAccountType()) {
            case SYSTEM:
            case ARTIST: {
                Specification<Facility> generalSpecification = new FacilitySpecification(filter);
                return facilityRepository.findAll(generalSpecification, pageable);
            }
            case REPRESENTATIVE: {
                Metadata organizationRoleMetadata = metadataService
                    .findByKeyAndAccountId(MetadataEnum.ORGANIZATION_ROLE.getValue(), currentAccountId);
                String organizationRole = organizationRoleMetadata.getValue();
                if (administrativeOrganizationRoles.contains(organizationRole)) {
                    Specification<Facility> generalSpecification = new FacilitySpecification(filter);
                    return facilityRepository.findAll(generalSpecification, pageable);
                } else {
                    Metadata facilityMetadata = metadataService
                        .findByKeyAndAccountId(MetadataEnum.FACILITY_ID.getValue(), currentAccountId);
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
        Account accountDto = accountService.findById(currentAccountId);


        switch (accountDto.getAccountType()) {
            case SYSTEM, ARTIST -> {
                return facilityRepository.findAll(new FacilitySpecification(filter));
            }
            case REPRESENTATIVE -> {
                Metadata organizationRoleMetadata = metadataService
                        .findByKeyAndAccountId(MetadataEnum.ORGANIZATION_ROLE.getValue(), currentAccountId);
                String organizationRole = organizationRoleMetadata.getValue();
                if (!administrativeOrganizationRoles.contains(organizationRole)) {
                    Metadata facilityMetadata = metadataService
                            .findByKeyAndAccountId(MetadataEnum.FACILITY_ID.getValue(), currentAccountId);
                    UUID facilityId = UUID.fromString(facilityMetadata.getValue());
                    filter.setId(facilityId);

                }
                return facilityRepository.findAll(new FacilitySpecification(filter));
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }
}

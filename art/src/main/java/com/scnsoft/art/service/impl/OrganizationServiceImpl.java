package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.OrganizationSpecification.facilityIdEqual;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.nameContain;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.organizationIdEquals;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.statusEquals;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.JoinType;

import com.scnsoft.art.dto.OrganizationFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.repository.OrganizationRepository;
import com.scnsoft.art.service.OrganizationService;

import static com.scnsoft.art.entity.Organization.Status.ACTIVE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Page<Organization> findAll(Pageable pageable, OrganizationFilter filter, Date inactiveDate) {
        Specification<Organization> specification = (root, cq, cb) -> {
            if (Boolean.TRUE.equals(filter.getWithFacilities())) {
                root.fetch(Organization.Fields.facilities, JoinType.INNER);
            }
            return cb.conjunction();
        };
        if(inactiveDate != null) {
            specification = specification.and((root, cq, cb) -> cb.and(
                    root.get(Organization.Fields.inactivationDate).isNotNull(),
                    cb.lessThan(root.get(Organization.Fields.inactivationDate), inactiveDate)
            ));
        }
        if (!(filter.getName() == null || filter.getName().isEmpty())) {
            specification = specification.and(nameContain(filter.getName()));
        }
        if (filter.getStatus() != null) {
            specification = specification.and(statusEquals(filter.getStatus()));
        }
        if(filter.getOrganizationId() != null) {
            specification = specification.and(organizationIdEquals(filter.getOrganizationId()));
        }
        if(filter.getFacilityId() != null) {
            specification = specification.and(facilityIdEqual(filter.getFacilityId()));
        }

        return organizationRepository.findAll(specification, pageable);
    }

    @Override
    public Organization findById(UUID id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization with id " + id + " not found"));
    }

    @Override
    public Organization findByAccountId(UUID accountId) {
        throw new UnsupportedOperationException();
    }

    public Organization findByName(String name) {
        return organizationRepository.findByName(name);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public Organization update(UUID id, Organization organization) {
        System.out.println(organization.toString());
        Organization existedOrganization = findById(id);
        var existingStatus = ACTIVE.equals(existedOrganization.getStatus());
        var updatedStatus = ACTIVE.equals(organization.getStatus());
        if(existingStatus && !updatedStatus) {
            organization.setInactivationDate(new Date());
        } else {
            organization.setInactivationDate(null);
        }
        organization.setId(id);
        organization.setFacilities(existedOrganization.getFacilities());

        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(UUID id) {
        organizationRepository.findById(id).ifPresent((org) -> {
            organizationRepository.delete(org);
            log.warn("Organization with {} {} was deleted", org.getName(), org.getId());
        });
    }

}

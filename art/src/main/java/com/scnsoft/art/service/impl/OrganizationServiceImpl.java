package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.OrganizationRepository;
import com.scnsoft.art.service.OrganizationService;

import static com.scnsoft.art.repository.specification.OrganizationSpecification.nameStartsWith;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.statusEquals;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    public Page<Organization> findAll(Pageable pageable, String name, String status) {
        System.out.println("name = " + name);
        System.out.println("status = " + status);
        Specification<Organization> specification = (root, cq, cb) -> {
            root.fetch("facilities", JoinType.INNER);
            return cb.conjunction();
        };

        if (!Strings.isNullOrEmpty(name)) {
            specification = specification.and(nameStartsWith(name));
        }
        if (!Strings.isNullOrEmpty(status)) {
            specification = specification.and(statusEquals(Organization.Status.valueOf(status)));
        }

        return organizationRepository.findAll(specification, pageable);
    }

    @Override
    public Organization findById(UUID id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new ArtResourceNotFoundException("Organization not found by id!"));
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
        organization.setId(id);
        organization.setFacilities(existedOrganization.getFacilities());

        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(UUID id) {
        organizationRepository.deleteById(id);
    }

}

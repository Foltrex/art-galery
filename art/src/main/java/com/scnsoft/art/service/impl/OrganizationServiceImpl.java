package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.OrganizationSpecification.nameContain;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.statusEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.JoinType;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.OrganizationRepository;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.OrganizationService;

import static com.scnsoft.art.entity.Organization.Status.ACTIVE;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.nameContain;
import static com.scnsoft.art.repository.specification.OrganizationSpecification.statusEquals;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final AccountFeignClient accountFeignClient;

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Page<Organization> findAll(Pageable pageable, String name, String status, Date inactiveDate) {
        System.out.println("name = " + name);
        System.out.println("status = " + status);

        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        AccountDto accountDto = accountFeignClient.findById(currentAccountId);

        Specification<Organization> specification = (root, cq, cb) -> {
            if (accountDto.getAccountType() == AccountType.REPRESENTATIVE) {
                root.fetch(Organization.Fields.facilities, JoinType.INNER);
            } else {
                root.fetch(Organization.Fields.facilities, JoinType.LEFT);
            }

            return cb.conjunction();
        };
        if(inactiveDate != null) {
            specification = specification.and((root, cq, cb) -> cb.and(
                    root.get(Organization.Fields.inactivationDate).isNotNull(),
                    cb.lessThan(root.get(Organization.Fields.inactivationDate), inactiveDate)
            ));
        }
        if (!Strings.isNullOrEmpty(name)) {
            specification = specification.and(nameContain(name));
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

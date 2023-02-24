package com.scnsoft.art.service;

import com.scnsoft.art.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizationService {

    Page<Organization> findAll(Pageable pageable);

    Organization findById(UUID id);

    Organization findByAccountId(UUID accountId);

    Organization save(Organization organization);

    Organization update(UUID id, Organization organization);

    void deleteById(UUID id);
}

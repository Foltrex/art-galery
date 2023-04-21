package com.scnsoft.art.service;

import com.scnsoft.art.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    List<Organization> findAll();
    Page<Organization> findAll(Pageable pageable, String name, String status);

    Organization findById(UUID id);

    Organization findByAccountId(UUID accountId);

    Organization save(Organization organization);

    Organization update(UUID id, Organization organization);

    Organization findByName(String name);
    void deleteById(UUID id);
}

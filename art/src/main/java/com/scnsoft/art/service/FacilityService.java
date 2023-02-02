package com.scnsoft.art.service;

import com.scnsoft.art.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FacilityService {

    Facility findById(UUID id);

    Page<Facility> findAll(Pageable pageable);

    Page<Facility> findAllByOrganizationId(UUID organizationId, Pageable pageable);

    Facility save(Facility facility);

    Facility updateById(UUID id, Facility facility);

    List<Facility> findAll();

    void deleteById(UUID id);

    Page<Facility> findAllByAccountId(UUID accountId, Pageable pageable);

    List<Facility> findAllByAccountId(UUID accountId);
}

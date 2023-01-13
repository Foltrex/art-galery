package com.scnsoft.art.service;

import com.scnsoft.art.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FacilityService {

    Page<Facility> findAll(Pageable pageable);

    Page<Facility> findAllByOrganizationId(UUID organizationId, Pageable pageable);

    Facility create(Facility facility);

    Facility updateById(Facility facility);

    void deleteById(UUID id);
}

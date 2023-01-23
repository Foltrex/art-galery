package com.scnsoft.art.service;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.impl.FacilityMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}

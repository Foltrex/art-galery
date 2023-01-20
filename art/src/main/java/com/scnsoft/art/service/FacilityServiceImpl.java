package com.scnsoft.art.service;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public Facility findById(UUID id) {
        return facilityRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    @Override
    public Page<Facility> findAll(Pageable pageable) {
        return facilityRepository.findAll(pageable);
    }

    @Override
    public Page<Facility> findAllByOrganizationId(UUID id, Pageable pageable) {
        Organization organization = organizationRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);

        return facilityRepository.findAllByOrganization(organization, pageable);
    }

    public Facility save(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Override
    public Facility updateById(UUID id, Facility facility) {
        facility.setId(id);
        return facilityRepository.save(facility);
    }

    public void deleteById(UUID id) {
        facilityRepository.deleteById(id);
    }
}

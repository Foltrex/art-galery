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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl {

    private final FacilityRepository facilityRepository;
    private final OrganizationRepository organizationRepository;
    private final FacilityMapper facilityMapper;

    public Page<Facility> findAll(Pageable pageable) {
        return facilityRepository.findAll(pageable);
    }

    public Page<Facility> findAllByOrganizationId(UUID id, Pageable pageable) {
        Organization organization = organizationRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);

        return facilityRepository.findAllByOrganization(organization, pageable);
    }

    public Facility findById(UUID id) {
        return facilityRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public FacilityDto save(FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        Facility persistedFacility = facilityRepository.save(facility);
        return facilityMapper.mapToDto(persistedFacility);
    }

    public FacilityDto update(UUID id, FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        facility.setId(id);
        Facility updatedFacility = facilityRepository.save(facility);
        return facilityMapper.mapToDto(updatedFacility);
    }

    public void deleteById(UUID id) {
        facilityRepository.deleteById(id);
    }
}

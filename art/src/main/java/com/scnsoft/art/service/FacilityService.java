package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.impl.FacilityMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;

@Service
public record FacilityService(
    FacilityRepository facilityRepository, 
    FacilityMapper facilityMapper
) {

    public List<FacilityDto> findAll() {
        return facilityRepository.findAll()
            .stream()
            .map(facilityMapper::mapToDto)
            .toList();
    }

    public FacilityDto findById(UUID id) {
        return facilityRepository
            .findById(id)
            .map(facilityMapper::mapToDto)
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

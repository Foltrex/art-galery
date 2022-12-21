package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;

@Service
public record FacilityService(FacilityRepository facilityRepository) {

    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    public Facility findById(UUID id) {
        return facilityRepository
            .findById(id)
            .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Facility save(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility update(UUID id, Facility facility) {
        facility.setId(id);
        return facilityRepository.save(facility);
    }

    public void deleteById(UUID id) {
        facilityRepository.deleteById(id);
    }
}

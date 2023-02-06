package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import com.scnsoft.art.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final RepresentativeRepository representativeRepository;
    private final OrganizationServiceImpl organizationService;

    @Override
    public Page<Facility> findAll(Pageable pageable) {
        return facilityRepository.findAll(pageable);
    }

    @Override
    public Page<Facility> findAllByOrganizationId(UUID id, Pageable pageable) {
        Organization organization = organizationService.findById(id);
        return facilityRepository.findAllByOrganization(organization, pageable);
    }

    @Override
    public Facility findById(UUID id) {
        return facilityRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    @Override
    public Facility save(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Override
    public Facility updateById(UUID id, Facility facility) {
        facility.setId(id);
        return facilityRepository.save(facility);
    }

    @Override
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        facilityRepository.deleteById(id);
    }

    @Override
    public Page<Facility> findAllByAccountId(UUID accountId, Pageable pageable) {
        Representative representative = representativeRepository
                .findByAccountId(accountId)
                .orElseThrow(IllegalArgumentException::new);

        Organization organization = representative.getOrganization();
        return facilityRepository.findAllByOrganization(organization, pageable);
    }

    @Override
    public List<Facility> findAllByAccountId(UUID accountId) {
        Representative representative = representativeRepository.findByAccountId(accountId)
                .orElseThrow(IllegalArgumentException::new);

        Organization organization = representative.getOrganization();
        return facilityRepository.findAllByOrganization(organization);
    }

}

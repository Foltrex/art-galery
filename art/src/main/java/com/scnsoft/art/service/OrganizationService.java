package com.scnsoft.art.service;

import com.scnsoft.art.dto.mapper.impl.OrganizationMapper;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.OrganizationRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record OrganizationService(OrganizationRepository organizationRepository,
                                  RepresentativeRepository representativeRepository,
                                  OrganizationMapper organizationMapper) {

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Organization findById(UUID id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Organization findByAccountId(UUID accountId) {
        Representative representative = getRepresentativeByAccountId(accountId);
        return representative.getOrganization();
    }

    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization update(UUID id, Organization organization) {
        organization.setId(id);
        return organizationRepository.save(organization);
    }

    public void deleteById(UUID id) {
        organizationRepository.deleteById(id);
    }

    private Representative getRepresentativeByAccountId(UUID accountId) {
        return representativeRepository
                .findByAccountId(accountId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }
}

package com.scnsoft.art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    List<Proposal> findByOrganization(Organization organization);

    Page<Proposal> findByArtist(Artist artist, Pageable pageable);

    Page<Proposal> findByFacility(Facility facility, Pageable pageable);

    long countByArtist(Artist artist);

    long countByFacility(Facility facility);
}

package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    List<Proposal> findByOrganization(Organization organization);

    Page<Proposal> findByArtist(Artist artist, Pageable pageable);

    List<Proposal> findByArt(Art art);

    Page<Proposal> findByFacility(Facility facility, Pageable pageable);

    long countByArtistAndArtistConfirmationIsNull(Artist artist);

    long countByFacilityAndArtistConfirmationIsNull(Facility facility);
}

package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID>, JpaSpecificationExecutor<Proposal> {

    List<Proposal> findByArt(Art art);

}

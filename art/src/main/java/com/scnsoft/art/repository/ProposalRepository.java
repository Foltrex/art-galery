package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    List<Proposal> findByOrganization(Organization organization);
}

package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {

}

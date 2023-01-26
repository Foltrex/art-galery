package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl {
    private final ProposalRepository proposalRepository;

    public Proposal findById(UUID proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

}

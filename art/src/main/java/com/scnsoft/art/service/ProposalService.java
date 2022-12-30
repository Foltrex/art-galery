package com.scnsoft.art.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;

    public Proposal findById(UUID proposalId) {
        return proposalRepository.findById(proposalId)
            .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

}

package com.scnsoft.art.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.FacilityRepository;
import com.scnsoft.art.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProposalServiceImpl {

    private final ProposalRepository proposalRepository;
    private final FacilityRepository facilityRepository;
    private final AccountFeignClient accountFeignClient;

    public Proposal findById(UUID proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    public Page<Proposal> findAllByAccountId(UUID accountId, Pageable pageable) {
        AccountDto accountDto = accountFeignClient.findById(accountId);
        switch (AccountType.fromString(accountDto.getAccountType())) {
            case ARTIST -> {
                Artist artist = artistRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);
                return proposalRepository.findByArtist(artist, pageable);
            }
            case REPRESENTATIVE -> {
                Representative representative = representativeRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);

                Facility faclity = facilityRepository.findByAccount
                return proposalRepository.findByFacility(representative.getFacility(), pageable);
            }
            case SYSTEM -> {
                return Page.empty(pageable);
            }
            default -> throw new IllegalArgumentException("Unknown account type");
        }
    }

    public long countByAccountId(UUID accountId) {
        AccountDto accountDto = accountFeignClient.findById(accountId);
        switch (AccountType.fromString(accountDto.getAccountType())) {
            case ARTIST -> {
                Artist artist = artistRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);

                return proposalRepository.countByArtistAccountIdAndArtistConfirmationIsNull(artist);
            }
            case REPRESENTATIVE -> {
                Representative representative = representativeRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);

                return proposalRepository.countByFacilityAndArtistConfirmationIsNull(representative.getFacility());
            }
            case SYSTEM -> {
                return 0;
            }
            default -> throw new IllegalArgumentException("Unknown account type");
        }
    }

    public void discardAllProposalsForArtExceptPassed(Art art, Proposal proposal) {
        List<Proposal> proposals = proposalRepository.findByArt(art);
        for (Proposal p : proposals) {
            if (!Objects.equals(proposal.getId(), p.getId())) {
                p.setArtistConfirmation(false);
                proposalRepository.save(p);
            }
        }
    }

    public void deleteById(UUID id) {
        proposalRepository.deleteById(id);
    }

}

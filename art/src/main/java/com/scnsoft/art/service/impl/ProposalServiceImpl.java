package com.scnsoft.art.service.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.ArtistRepository;
import com.scnsoft.art.repository.ProposalRepository;
import com.scnsoft.art.repository.RepresentativeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl {
    private static final String REPRESENTATIVE_ACCOUNT_TYPE = "REPRESENTATIVE";
    private static final String ARTIST_ACCOUNT_TYPE = "ARTIST";

    private final ProposalRepository proposalRepository;
    private final ArtistRepository artistRepository;
    private final RepresentativeRepository representativeRepository;
    private final AccountFeignClient accountFeignClient;

    public Proposal findById(UUID proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    public Page<Proposal> findAllByAccountId(UUID accountId, Pageable pageable) {
        ResponseEntity<AccountDto> accountResponse = accountFeignClient.findById(accountId);
        if (accountResponse.getBody() != null) {
            AccountDto accountDto = accountResponse.getBody();

            return switch (accountDto.getAccountType()) {
                case ARTIST_ACCOUNT_TYPE -> { 
                    Artist artist = artistRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);

                    yield proposalRepository.findByArtist(artist, pageable);
                }
                case REPRESENTATIVE_ACCOUNT_TYPE -> {
                    Representative representative = representativeRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);   

                    yield proposalRepository.findByFacility(representative.getFacility(), pageable);
                }
                default -> throw new IllegalArgumentException("Unknown account type");
            };
        } else {
            return Page.empty(pageable);
        }
    }

    public long countByAccountId(UUID accountId) {
        ResponseEntity<AccountDto> accountResponse = accountFeignClient.findById(accountId);
        if (accountResponse.getBody() != null) {
            AccountDto accountDto = accountResponse.getBody();

            return switch (accountDto.getAccountType()) {
                case ARTIST_ACCOUNT_TYPE -> { 
                    Artist artist = artistRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);

                    yield proposalRepository.countByArtist(artist);
                }
                case REPRESENTATIVE_ACCOUNT_TYPE -> {
                    Representative representative = representativeRepository.findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);   

                    yield proposalRepository.countByFacility(representative.getFacility());
                }
                default -> throw new IllegalArgumentException("Unknown account type");
            };
        } else {
            return 0;
        }
    }

    public void discardAllProposalsForArtExceptPassed(Art art, Proposal proposal) {
        proposalRepository.findByArt(art)
            .stream()
            .filter(p -> !Objects.equals(proposal.getId(), p.getId()))
            .peek(p -> {
                p.setArtistConfirmation(false);
                proposalRepository.save(p);
            });
    }

    public void deleteById(UUID id) {
        proposalRepository.deleteById(id);
    }

}

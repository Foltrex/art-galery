package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.repository.ArtistRepository;
import com.scnsoft.art.repository.ProposalRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.service.ArtService;

import lombok.extern.slf4j.Slf4j;

@Service
public record ArtServiceImpl(
        ArtRepository artRepository,
        ArtistRepository artistRepository,
        RepresentativeRepository representativeRepository,
        ArtInfoRepository artInfoRepository,
        AccountFeignClient accountFeignClient,
        ArtMapper artMapper
) implements ArtService {
    private static final String ARTIST_ACCOUNT_TYPE = "ARTIST";
    private static final String REPRESENTATIVE_ACCOUNT_TYPE = "REPRESENTATIVE";

    public List<Art> findAll() {
        return artRepository.findAll();
    }

    public Art findById(UUID id) {
        return artRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public Art save(Art art) {
        return artRepository.save(art);
    }

    public Art update(UUID id, Art art) {
        art.setId(id);
        return artRepository.save(art);
    }

    public void deleteById(UUID id) {
        artRepository.deleteById(id);
    }

    @Override
    public Page<Art> findAllByAccountIdAndName(UUID accountId, Pageable pageable, String artName) {
        AccountDto accountDto = accountFeignClient.findById(accountId).getBody();

        if (accountDto != null) {
            String accountType = accountDto.getAccountType();
            switch (accountType) {
                case ARTIST_ACCOUNT_TYPE: {
                    Artist artist = artistRepository
                        .findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);
                    
                    return !Strings.isNullOrEmpty(artName) 
                        ? artRepository.findAllByArtistAndName(artist, pageable, artName)
                        : artRepository.findAllByArtist(artist, pageable);
                }
                case REPRESENTATIVE_ACCOUNT_TYPE: {
                    return !Strings.isNullOrEmpty(artName)
                        ? artRepository.findAllByProposalIsNullAndArtInfoIsNull(pageable, artName)
                        : artRepository.findAllByProposalIsNullAndArtInfoIsNull(pageable);
                }
                default: {
                    throw new IllegalArgumentException("Unknown account");
                }
            }
        } else {
            return Page.empty();
        }
    }
}

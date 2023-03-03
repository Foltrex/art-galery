package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artInfosIsEmpty;
import static com.scnsoft.art.repository.specification.ArtSpecification.nameIsEqualTo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.repository.ArtistRepository;
import com.scnsoft.art.repository.RepresentativeRepository;
import com.scnsoft.art.service.ArtService;

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
    public Page<Art> findAllByAccountIdAndName(UUID accountId, Pageable pageable, String artName, boolean isExhibited) {
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
                    Representative representative = representativeRepository
                        .findByAccountId(accountId)
                        .orElseThrow(ArtResourceNotFoundException::new);


                    if (!isExhibited) {
                        return !Strings.isNullOrEmpty(artName)
                            ? artRepository.findAll(artInfosIsEmpty().and(nameIsEqualTo(artName)), pageable)
                            : artRepository.findAll(artInfosIsEmpty(), pageable);
                            
                    } else {
                        return !Strings.isNullOrEmpty(artName)
                            ? artRepository.findAll(nameIsEqualTo(artName), pageable)
                            : artRepository.findAll(pageable);
                    }

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

package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.artistIdEqual;
import static com.scnsoft.art.repository.specification.ArtSpecification.cityIdEquals;
import static com.scnsoft.art.repository.specification.ArtSpecification.descriptionContain;

import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.ArtFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.feignclient.AccountFeignClient;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.service.FileService;

@Service
public record ArtServiceImpl(
        ArtRepository artRepository,
        ArtInfoRepository artInfoRepository,
        AccountFeignClient accountFeignClient,
        ArtMapper artMapper,
        FileService fileService
) implements ArtService {
    private static final String EXHIBITED_ARTS = "exhibited";
    private static final String FREE_ARTS = "free";

    private static final String SEARCH_BY_ART_NAME = "art name";
    private static final String SEARCH_BY_ARTIST_NAME = "artist name";
    private static final String SEARCH_BY_CITY = "city";
    private static final String SEARCH_BY_DESCRIPTION = "description";

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
    public Page<Art> findAll(
            Pageable pageable,
            ArtFilter artFilter
    ) {
        Specification<Art> specification = Specification.where(null);


        if (artFilter.getCityId() != null) {
            specification = specification.and(cityIdEquals(artFilter.getCityId()));
        }

        if (!Strings.isNullOrEmpty(artFilter.getSearchText())) {
            Specification<Art> artNameOrDescription = artNameContain(artFilter.getSearchText())
                    .or(descriptionContain(artFilter.getSearchText()));

            specification = specification.and(artNameOrDescription);
        }

        if(artFilter.getArtistId() != null) {
            specification = specification.and(artistIdEqual(artFilter.getArtistId()));
        }


        return artRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteByAccountId(UUID accountId) {
        artRepository.findByArtistAccountId(accountId).forEach(art -> {
            fileService.deleteByArtId(art.getId());
            this.deleteById(art.getId());
        });
    }
}

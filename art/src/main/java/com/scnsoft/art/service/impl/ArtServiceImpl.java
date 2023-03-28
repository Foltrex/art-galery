package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artInfosIsEmpty;
import static com.scnsoft.art.repository.specification.ArtSpecification.artNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.artistNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.cityNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.decriptionContain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
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
    public Page<Art> findAllByParameters(
        Pageable pageable, 
        String searchText, 
        String searchFilter, 
        String searchOption
    ) {
        Specification<Art> searchFilterSpecification = switch (searchFilter) {
            case EXHIBITED_ARTS -> Specification.not(artInfosIsEmpty());
            case FREE_ARTS -> artInfosIsEmpty();
            default -> Specification.where(null);
        };


        return Strings.isNullOrEmpty(searchText)
            ? artRepository.findAll(searchFilterSpecification, pageable)
            : switch (searchOption) {
                case SEARCH_BY_ART_NAME: {
                    Specification<Art> artNameSpecification = searchFilterSpecification.and(artNameContain(searchText));
                    yield artRepository.findAll(artNameSpecification, pageable);
                }
                case SEARCH_BY_ARTIST_NAME: {
                    Specification<Art> artistNameSpecification = searchFilterSpecification.and(artistNameContain(searchText));
                    yield artRepository.findAll(artistNameSpecification, pageable);
                }
                case SEARCH_BY_CITY: {
                    Specification<Art> citySpecification = searchFilterSpecification.and(cityNameContain(searchText));
                    yield artRepository.findAll(citySpecification, pageable);
                }
                case SEARCH_BY_DESCRIPTION: {
                    Specification<Art> descriptionSpecification = searchFilterSpecification.and(decriptionContain(searchText));
                    yield artRepository.findAll(descriptionSpecification, pageable);
                }
                default: {
                    yield artRepository.findAll(pageable);
                }
            };
    }

    @Override
    public Page<Art> findAllByArtistId(UUID artistId, Pageable pageable) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow();

        return artRepository.findAllByArtist(artist, pageable);
    }

    @Override
    public Page<Art> findAll(
        Pageable pageable, 
        String artistName, 
        String cityName, 
        String artNameAndDescription
    ) {
        Specification<Art> specification = Specification.where(null);

        if (!Strings.isNullOrEmpty(artistName)) {
            specification = specification.and(artistNameContain(artistName));
        }

        if (!Strings.isNullOrEmpty(cityName)) {
            specification = specification.and(cityNameContain(cityName));
        }

        if (!Strings.isNullOrEmpty(artNameAndDescription)) {
            Specification<Art> artNameOrDescription = artNameContain(artNameAndDescription)
                .or(decriptionContain(artNameAndDescription));

            specification = specification.and(artNameOrDescription);
        }

        return artRepository.findAll(specification, pageable);
    }
}

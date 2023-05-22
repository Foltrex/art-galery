package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.artistIdEqual;
import static com.scnsoft.art.repository.specification.ArtSpecification.cityIdEquals;
import static com.scnsoft.art.repository.specification.ArtSpecification.descriptionContain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.service.FileServiceImplFile;
import com.scnsoft.art.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.service.FileService;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtServiceImpl implements ArtService {

    @Value("${app.props.images.original_width}")
    private Integer imageOriginalWidth;

    @Value("${app.props.images.original_height}")
    private Integer imageOriginalHeight;

    @Value("${app.props.images.thumbnail_width}")
    private Integer imageThumbnailWidth;

    @Value("${app.props.images.thumbnail_height}")
    private Integer imageThumbnailHeight;

    private final ImageService imageService;
    private final ArtRepository artRepository;
    private final FileService fileService;
    private final FileServiceImplFile fileServiceImpl;


    public List<Art> findAll() {
        return artRepository.findAll();
    }

    public Art findById(UUID id) {
        return artRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Art with id " + id + " not found"));
    }

    public Art save(Art art, List<EntityFile> entityFiles) {
        if(art.getId() != null) {
            Art original = findById(art.getId());
            art.setArtistAccountId(original.getArtistAccountId());
        }
        Art saved = artRepository.save(art);

        imageService.processImages(
                saved.getId(),
                entityFiles,
                "/art/" + art.getId() + "/",
                imageOriginalWidth,
                imageOriginalHeight,
                imageThumbnailWidth,
                imageThumbnailHeight);

        return saved;
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

        if (!(artFilter.getSearchText() == null || artFilter.getSearchText().isEmpty())) {
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

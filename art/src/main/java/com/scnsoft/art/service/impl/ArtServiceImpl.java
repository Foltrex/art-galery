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
import com.scnsoft.art.entity.ArtStyle;
import com.scnsoft.art.entity.ArtTopic;
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

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

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

        entityFiles.stream()
                .filter(f -> f.getOriginalId() == null && Boolean.TRUE.equals(f.getIsPrimary()))
                .findAny().orElseGet(() -> {
                    EntityFile newPrimary = null;
                    for(EntityFile f : entityFiles) {
                        if(f.getOriginalId() == null) {
                            //update first as a primary
                            f.setIsPrimary(true);
                            newPrimary = f;
                            break;
                        }
                    }
                    if(newPrimary != null) {
                        for(EntityFile f : entityFiles) {
                            if(newPrimary.getId().equals(f.getOriginalId())) {
                                //update thumbnail
                                f.setIsPrimary(true);
                                break;
                            }
                        }
                    }
                    return newPrimary;
                });

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

        if(artFilter.getArtFormat() != null && !artFilter.getArtFormat().isEmpty()) {
            specification = specification.and((r, q, cb) ->
                    r.get(Art.Fields.artFormat).in(artFilter.getArtFormat()));
        }
        if(artFilter.getPrice() != null && artFilter.getPrice().size() == 2) {
            specification = specification.and((r, q, cb) ->
                    cb.between(
                            r.get(Art.Fields.price),
                            artFilter.getPrice().get(0),
                            artFilter.getPrice().get(1)));
        }
        if(artFilter.getArtSize() != null && !artFilter.getArtSize().isEmpty()) {
            specification = specification.and((r, q, cb) ->
                    r.get(Art.Fields.artSize).in(artFilter.getArtSize()));
        }
        if(artFilter.getArtStyle() != null && !artFilter.getArtStyle().isEmpty()) {
            specification = specification.and((r, q, cb) -> {
                Join<Art , ArtStyle> join = r.join(Art.Fields.artStyles, JoinType.INNER);
                return join.get(ArtStyle.Fields.id).in(artFilter.getArtStyle());
            });
        }
        if(artFilter.getArtTopic() != null && !artFilter.getArtTopic().isEmpty()) {
            specification = specification.and((r, q, cb) -> {
                Join<Art , ArtTopic> join = r.join(Art.Fields.artTopics, JoinType.INNER);
                return join.get(ArtTopic.Fields.id).in(artFilter.getArtTopic());
            });
        }
        if(artFilter.getArtType() != null && !artFilter.getArtType().isEmpty()) {
            specification = specification.and((r, q, cb) ->
                    r.get(Art.Fields.artType).in(artFilter.getArtType()));
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

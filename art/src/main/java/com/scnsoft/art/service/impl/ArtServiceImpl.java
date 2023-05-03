package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.artistIdEqual;
import static com.scnsoft.art.repository.specification.ArtSpecification.cityIdEquals;
import static com.scnsoft.art.repository.specification.ArtSpecification.descriptionContain;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.EntityFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.service.FileService;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;

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

    private final ArtRepository artRepository;
    private final FileService fileService;


    public List<Art> findAll() {
        return artRepository.findAll();
    }

    public Art findById(UUID id) {
        return artRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Art with id " + id + " not found"));
    }

    public Art save(Art art) {
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
    public EntityFile uploadImage(UUID id, UploadEntityFileDto uploadEntityFileDto) {
        Art art = findById(id);

        uploadEntityFileDto.setEntityId(art.getId());

        byte[] decodedImageData = Base64.getDecoder().decode(uploadEntityFileDto.getData());

        byte[] original = cutImage(decodedImageData, uploadEntityFileDto.getMimeType(), imageOriginalWidth, imageOriginalHeight)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload image"));
        byte[] thumb = cutImage(decodedImageData, uploadEntityFileDto.getMimeType(), imageThumbnailWidth, imageThumbnailHeight)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload image"));

        uploadEntityFileDto.setData(Base64.getEncoder().encodeToString(original));

        EntityFile result = fileService.uploadFile(uploadEntityFileDto, EntityFile.Type.ORIGINAL, null);

        uploadEntityFileDto.setData(Base64.getEncoder().encodeToString(thumb));
        uploadEntityFileDto.setIsPrimary(result.getIsPrimary());
        fileService.uploadFile(uploadEntityFileDto, EntityFile.Type.THUMBNAIL, result.getId());

        return result;
    }

    @Override
    public void deleteByAccountId(UUID accountId) {
        artRepository.findByArtistAccountId(accountId).forEach(art -> {
            fileService.deleteByArtId(art.getId());
            this.deleteById(art.getId());
        });
    }

    private Optional<byte[]> cutImage(byte[] imageData, String mimeType, Integer cutWidth, Integer cutHeight) {
        Optional<String> typeOpt = getImageType(mimeType);
        if(typeOpt.isEmpty()) {
            return Optional.empty();
        }
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            BufferedImage before = ImageIO.read(in);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            if (before.getWidth() > cutWidth || before.getHeight() > cutHeight) {
                int w = before.getWidth();
                int h = before.getHeight();
                double scaleX = before.getWidth() > cutWidth ? ((double)cutWidth) / w : 1;
                double scaleY = before.getHeight() > cutHeight ? ((double)cutHeight) / h : 1;
                double scale = Math.min(scaleY, scaleX);

                int newWidth, newHeight;
                if(scaleY < scaleX) {
                    newHeight = cutHeight;
                    newWidth = (int)(w * scale);
                } else {
                    newHeight = (int)(h * scale);
                    newWidth = cutWidth;
                }
                Thumbnails.of(before)
                        .size(newWidth, newHeight)
                        .outputFormat(typeOpt.get()).toOutputStream(buffer);
            } else {
                Thumbnails.of(before)
                        .size(before.getWidth(), before.getHeight())
                        .outputFormat(typeOpt.get()).toOutputStream(buffer);
            }
            imageData = buffer.toByteArray();

        } catch (IOException e) {
            throw new ServiceException("Cannot cut image");
        }
        return Optional.of(imageData);
    }

    private Optional<String> getImageType(String mime) {
        if(mime.isEmpty()) {
            return Optional.empty();
        }
        try {
            String type = MimeType.valueOf(mime).getSubtype().toLowerCase();
            switch (type) {
                case "jpeg":
                case "jpg":
                    return Optional.of("jpg");
                case "png":
                case "bmp":
                case "gif":
                    return Optional.of(type);
                default:
                    log.error("Failed to parse mime type from " + mime);
                    return Optional.of(type);
            }
        } catch (InvalidMimeTypeException e) {
            log.error("Failed to parse mime type from " + mime, e);
            return Optional.empty();
        }
    }
}

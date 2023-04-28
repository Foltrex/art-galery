package com.scnsoft.art.service.impl;

import static com.scnsoft.art.repository.specification.ArtSpecification.artNameContain;
import static com.scnsoft.art.repository.specification.ArtSpecification.artistIdEqual;
import static com.scnsoft.art.repository.specification.ArtSpecification.cityIdEquals;
import static com.scnsoft.art.repository.specification.ArtSpecification.descriptionContain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.EntityFile;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtRepository;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.service.FileService;
import org.springframework.util.MimeType;

import javax.imageio.ImageIO;

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
                .orElseThrow(ArtResourceNotFoundException::new);
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
        Art art = artRepository.findById(id).orElseThrow(ArtResourceNotFoundException::new);
        uploadEntityFileDto.setEntityId(art.getId());

        byte[] decodedImageData = Base64.getDecoder().decode(uploadEntityFileDto.getData());

        byte[] original = cutImage(decodedImageData, uploadEntityFileDto.getMimeType(), imageOriginalWidth, imageOriginalHeight);
        byte[] thumb = cutImage(decodedImageData, uploadEntityFileDto.getMimeType(), imageThumbnailWidth, imageThumbnailHeight);

        uploadEntityFileDto.setData(Base64.getEncoder().encodeToString(original));

        EntityFile result = fileService.uploadFile(uploadEntityFileDto, EntityFile.Type.ORIGINAL, null);

        uploadEntityFileDto.setData(Base64.getEncoder().encodeToString(thumb));
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

    private byte[] cutImage(byte[] imageData, String mimeType, Integer cutWidth, Integer cutHeight) {
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            BufferedImage img = ImageIO.read(in);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            if (img.getWidth() > cutWidth || img.getHeight() > cutHeight) {
                BufferedImage croppedImage = img.getSubimage(
                        0, 0,
                        Math.min(img.getWidth(), cutWidth),
                        Math.min(img.getHeight(), cutHeight)
                );
                ImageIO.write(croppedImage, MimeType.valueOf(mimeType).getSubtype(), buffer);
            } else {
                ImageIO.write(img, MimeType.valueOf(mimeType).getSubtype(), buffer);
            }
            imageData = buffer.toByteArray();

        } catch (IOException e) {
            throw new ServiceException("Cannot cut image");
        }
        return imageData;
    }
}

package com.scnsoft.file.service;

import com.scnsoft.file.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${app.props.images.path}")
    private String imagesPath;

    @Value("${app.props.images.width}")
    private Integer imageWidth;

    @Value("${app.props.images.height}")
    private Integer imageHeight;

    private final DocumentService documentService;

    public void uploadAvatar(ImageDto imageDto) {
        byte[] decodedImageData = Base64.getDecoder().decode(imageDto.getImageData());

        String imagePath = buildImagePath(imageDto.getArtId(), imageDto.getFilename(), imageDto.getMimeType());

//        byte[] croppedImage = cutImage(decodedImageData, imageDto.getMimeType());

        documentService.upload(imagePath, decodedImageData, imageDto.getArtId());
    }

    private String buildImagePath(Long artId, String filename, String mimeType) {
        String userIdRage = getUserIdRange(artId);
        String imageExtension = MimeType.valueOf(mimeType).getSubtype();


        StringBuilder imagePath = new StringBuilder
                (String.format("%s/%s/%d/%s.%s", imagesPath, userIdRage, artId, filename, imageExtension));

        File tempFile = new File(String.valueOf(imagePath));
//        if (tempFile.exists()) {
//            imagePath.append()
//        }

        String folderPath = String.format("%s/%s/%d/", imagesPath, userIdRage, artId);

        int count = Objects.requireNonNull(new File(folderPath).list()).length;
        System.out.println(count);

        return String.format("%s/%s/%d/%d.%s", imagesPath, userIdRage, artId, ++count, imageExtension);
    }

    private String getUserIdRange(Long userId) {
        long count = (userId % 1000 != 0)
                ? (userId / 1000) * 1000 + 1
                : (userId / 1000 - 1) * 1000 + 1;

        return count + "_" + (count + 999);
    }

    private byte[] cutImage(byte[] imageData, String mimeType) {
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            BufferedImage img = ImageIO.read(in);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            if (img.getWidth() > imageWidth || img.getHeight() > imageHeight) {
                BufferedImage croppedImage = img.getSubimage(
                        0, 0,
                        Math.min(img.getWidth(), imageWidth),
                        Math.min(img.getHeight(), imageHeight)
                );
                ImageIO.write(croppedImage, MimeType.valueOf(mimeType).getSubtype(), buffer);
            } else {
                ImageIO.write(img, MimeType.valueOf(mimeType).getSubtype(), buffer);
            }
            imageData = buffer.toByteArray();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cut image");
        }
        return imageData;
    }
}

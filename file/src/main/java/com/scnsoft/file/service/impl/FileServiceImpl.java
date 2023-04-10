package com.scnsoft.file.service.impl;

import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.entity.FileInfo;
import com.scnsoft.file.exception.ResourseNotFoundException;
import com.scnsoft.file.repository.FileInfoRepository;
import com.scnsoft.file.service.DocumentService;
import com.scnsoft.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${app.props.images.path}")
    private String pathToFiles;

    @Value("${app.props.images.original_width}")
    private Integer imageOriginalWidth;

    @Value("${app.props.images.original_height}")
    private Integer imageOriginalHeight;

    @Value("${app.props.images.thumbnail_width}")
    private Integer imageThumbnailWidth;

    @Value("${app.props.images.thumbnail_height}")
    private Integer imageThumbnailHeight;

    private final static String SEPARATOR = FileSystems.getDefault().getSeparator();

    private final DocumentService documentService;
    private final FileInfoRepository fileInfoRepository;

    @Override
    public Optional<FileInfo> findFileInfoById(UUID id) {
        return fileInfoRepository
                .findById(id);
    }

    @Override
    public InputStream getFileStream(UUID id) {
        FileInfo fileInfo = findFileInfoById(id)
                .orElseThrow(() -> new ResourseNotFoundException("File not found!"));
        String filePath = generateFilePath(fileInfo.getId());

        return documentService.getInputStream(filePath);
    }

    @Override
    public List<FileInfo> uploadFile(UploadFileDto uploadFileDto) {
        byte[] decodedImageData = Base64.getDecoder().decode(uploadFileDto.getData());

        FileInfo fileOriginalInfo = prepareAndSaveFile(decodedImageData, uploadFileDto.getMimeType(),
                imageOriginalWidth,
                imageOriginalHeight);

        FileInfo fileThumbnailInfo = prepareAndSaveFile(decodedImageData, uploadFileDto.getMimeType(),
                imageThumbnailWidth,
                imageThumbnailHeight);

        return List.of(fileOriginalInfo, fileThumbnailInfo);
    }

    @Override
    public void removeFileById(UUID id) {
        findFileInfoById(id).ifPresent(this::deleteFile);
    }

    //@TODO REMOVE
    @Override
    public List<FileInfo> findAllByArtId(UUID artId) {
        return null;
    }

    //@TODO REMOVE
    public void deleteByArtId(UUID artId) {
//        fileInfoRepository.findAllByArtId(artId)
//                .forEach(this::deleteFile);
    }

    private String generateFilePath(UUID id) {
        String str = id.toString();
        return pathToFiles + str.substring(0, 3) + SEPARATOR + str.substring(3, 6) + SEPARATOR + id;
    }

    private void deleteFile(FileInfo fileInfo) {
        String filePath = generateFilePath(fileInfo.getId());
        fileInfoRepository.delete(fileInfo);
        documentService.remove(filePath);
    }

    private FileInfo prepareAndSaveFile(byte[] decodedImageData, String mimeType, Integer imageWidth, Integer imageHeight) {
        byte[] croppedImage = cutImage(decodedImageData, mimeType, imageWidth, imageHeight);

        FileInfo fileInfo = FileInfo.builder()
                .mimeType(mimeType)
                .contentLength(croppedImage.length)
                .build();

        fileInfo = fileInfoRepository.save(fileInfo);

        String path = generateFilePath(fileInfo.getId());

        documentService.upload(path, croppedImage);

        return fileInfo;
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

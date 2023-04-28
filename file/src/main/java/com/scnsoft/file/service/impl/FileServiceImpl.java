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
    public FileInfo uploadFile(UploadFileDto uploadFileDto) {
        byte[] decodedImageData = Base64.getDecoder().decode(uploadFileDto.getData());

        FileInfo fileInfo = FileInfo.builder()
                .mimeType(uploadFileDto.getMimeType())
                .contentLength(decodedImageData.length)
                .build();

        fileInfo = fileInfoRepository.save(fileInfo);

        String path = generateFilePath(fileInfo.getId());

        documentService.upload(path, decodedImageData);

        return fileInfo;
    }

    @Override
    public void removeFileById(UUID id) {
        findFileInfoById(id).ifPresent(this::deleteFile);
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

}

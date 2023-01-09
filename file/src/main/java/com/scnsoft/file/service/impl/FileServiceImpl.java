package com.scnsoft.file.service.impl;

import com.scnsoft.file.dto.FileStreamDto;
import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.entity.FileInfo;
import com.scnsoft.file.exception.ResourseNotFoundException;
import com.scnsoft.file.repository.FileInfoRepository;
import com.scnsoft.file.service.DocumentService;
import com.scnsoft.file.service.FileService;
import com.scnsoft.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${app.props.images.path}")
    private String pathToFiles;

    private final DocumentService documentService;
    private final FileInfoRepository fileInfoRepository;

    @Override
    public FileInfo findFileInfoById(UUID id) {
        return fileInfoRepository
                .findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("FileInfo not found!"));
    }

    @Override
    public Page<FileInfo> findAllFileInfoByArtId(UUID artId, Pageable pageable) {
        return fileInfoRepository.findAllByArtId(artId, pageable);
    }

    @Override
    public FileStreamDto getFileStream(UUID id) {
        FileInfo fileInfo = findFileInfoById(id);
        String filePath = buildFilePath(fileInfo);

        InputStream inputStream = documentService.getInputStream(filePath);

        return FileStreamDto.builder().inputStream(inputStream).build();
    }

    @Override
    @Transactional
    public FileInfo uploadFile(UploadFileDto uploadFileDto) {
        byte[] decodedImageData = Base64.getDecoder().decode(uploadFileDto.getData());
        String filePath = FileUtil.generateNewFilePath(uploadFileDto.getArtId(), uploadFileDto.getMimeType(), pathToFiles);

        FileInfo fileInfo = FileInfo.builder()
                .artId(uploadFileDto.getArtId())
                .mimeType(uploadFileDto.getMimeType())
                .filename(uploadFileDto.getFilename())
                .systemFileName(FileUtil.getSystemFilenameFromPath(filePath))
                .build();

        fileInfo = fileInfoRepository.save(fileInfo);

        documentService.upload(filePath, decodedImageData);

        return fileInfo;
    }

    @Override
    @Transactional
    public void removeFileById(UUID id) {
        FileInfo fileInfo = findFileInfoById(id);
        String filePath = buildFilePath(fileInfo);

        fileInfoRepository.delete(fileInfo);
        documentService.remove(filePath);
    }

    private String buildFilePath(FileInfo fileInfo) {
        return FileUtil.buildFilePath(
                fileInfo.getArtId(),
                fileInfo.getSystemFileName(),
                fileInfo.getMimeType(),
                pathToFiles);
    }

}

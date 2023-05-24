package com.scnsoft.art.service;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.repository.FileInfoRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImplFile {

    public final static String temp = "/temp/";

    public record FileWrapper(FileInfo fileInfo, InputStream inputStream){}

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileFilter {
        private String dir;
        private Date createdAt;
    }

    @Value("${app.props.images.path}")
    private String pathToFiles;


    private final static String SEPARATOR = FileSystems.getDefault().getSeparator();

    private final DocumentService documentService;
    private final FileInfoRepository fileInfoRepository;

    public Optional<FileInfo> findFileInfoById(UUID id) {
        return fileInfoRepository
                .findById(id);
    }

    public List<FileInfo> findFiles(FileFilter fileFilter) {
        return fileInfoRepository
                .findAll((root, q, cb) -> {
                    var predicates = new ArrayList<Predicate>();
                    if(fileFilter.getCreatedAt() != null) {
                        predicates.add(cb.greaterThan(root.get(FileInfo.Fields.createdAt), fileFilter.getCreatedAt()));
                    }
                    if(fileFilter.getDir() != null) {
                        predicates.add(cb.like(root.get(FileInfo.Fields.directory), fileFilter.getDir()));
                    }
                    return cb.and(predicates.toArray(Predicate[]::new));
                });
    }

    public FileWrapper getFileStream(UUID id) {
        FileInfo fileInfo = findFileInfoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found!"));
        String filePath = generateFilePath(fileInfo.getId());

        return new FileWrapper(fileInfo, documentService.getInputStream(filePath));
    }

    @Transactional
    public FileInfo upstream(FileInfo fileInfo, byte[] b) {
        if(fileInfo.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upstream new file");
        }
        if(b != null) {
            String path = generateFilePath(fileInfo.getId());
            documentService.save(path, b);
            fileInfo.setContentLength(b.length);
        }
        fileInfo.setOriginalName(trimFileName(fileInfo.getOriginalName()));
        return fileInfoRepository.save(fileInfo);
    }

    @Transactional
    public FileInfo uploadFile(FileInfoDto uploadFileDto) {
        byte[] decodedImageData = Base64.getDecoder().decode(uploadFileDto.getData());

        FileInfo fileInfo = FileInfo.builder()
                .mimeType(uploadFileDto.getMimeType())
                .contentLength(decodedImageData.length)
                .directory(uploadFileDto.getDirectory())
                .originalName(uploadFileDto.getOriginalName())
                .cacheControl(uploadFileDto.getCacheControl())
                .build();

        fileInfo.setOriginalName(trimFileName(fileInfo.getOriginalName()));

        fileInfo = fileInfoRepository.save(fileInfo);

        String path = generateFilePath(fileInfo.getId());

        documentService.save(path, decodedImageData);

        return fileInfo;
    }

    @Transactional
    public void deleteById(UUID id) {
        findFileInfoById(id).ifPresent(this::deleteFile);
    }

    @Transactional
    public void deleteFile(FileInfo fileInfo) {
        fileInfoRepository.deleteById(fileInfo.getId());
        documentService.remove(generateFilePath(fileInfo.getId()));
    }

    private String generateFilePath(UUID id) {
        String str = id.toString();
        return pathToFiles + str.substring(0, 3) + SEPARATOR + str.substring(3, 6) + SEPARATOR + id;
    }

    private String trimFileName(String name) {
        if(name == null) {
            return null;
        }
        if(name.length() < 50) {
            return name;
        }
        var ext = name.lastIndexOf(".");
        if(ext == -1) {
            return name.substring(0, 50);
        }
        var extLength = name.length() - ext;
        return name.substring(0, 50 - extLength) + name.substring(ext);
    }

}

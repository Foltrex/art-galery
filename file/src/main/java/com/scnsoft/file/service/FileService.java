package com.scnsoft.file.service;

import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.entity.FileInfo;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    Optional<FileInfo> findFileInfoById(UUID id);

    InputStream getFileStream(UUID ids);

    FileInfo uploadFile(UploadFileDto uploadFileDto);

    void removeFileById(UUID id);

}

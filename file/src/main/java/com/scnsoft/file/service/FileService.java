package com.scnsoft.file.service;

import com.scnsoft.file.dto.FileStreamDto;
import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FileService {

    FileInfo findFileInfoById(UUID id);

    Page<FileInfo> findAllFileInfoByArtId(UUID artId, Pageable pageable);

    FileStreamDto getFileStream(UUID id);

    FileInfo uploadFile(UploadFileDto uploadFileDto);

    void removeFileById(UUID id);

}

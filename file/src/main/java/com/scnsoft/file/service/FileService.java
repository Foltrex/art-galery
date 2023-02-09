package com.scnsoft.file.service;

import com.scnsoft.file.dto.FileStreamDto;
import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FileService {

    FileInfo findFileInfoById(UUID id);

    Page<FileInfo> findAllFileInfoByArtId(UUID artId, Pageable pageable);

    List<FileInfo> findAllByArtId(UUID artId);

    List<FileInfo> findAllFirstByArtId(List<UUID> artId);

    List<FileStreamDto> getFileStream(List<UUID> ids);

    FileInfo uploadFile(UploadFileDto uploadFileDto);

    void removeFileById(UUID id);

}

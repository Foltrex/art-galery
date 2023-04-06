package com.scnsoft.file.dto.mapper.impl;

import com.scnsoft.file.dto.FileInfoDto;
import com.scnsoft.file.dto.mapper.Mapper;
import com.scnsoft.file.entity.FileInfo;
import org.springframework.stereotype.Component;

@Component
public class FileInfoMapper implements Mapper<FileInfo, FileInfoDto> {

    @Override
    public FileInfoDto mapToDto(FileInfo fileInfo) {
        return FileInfoDto.builder()
                .id(fileInfo.getId())
                .mimeType(fileInfo.getMimeType())
                .contentLength(fileInfo.getContentLength())
                .build();
    }

    @Override
    public FileInfo mapToEntity(FileInfoDto fileInfoDto) {
        return FileInfo.builder()
                .id(fileInfoDto.getId())
                .mimeType(fileInfoDto.getMimeType())
                .contentLength(fileInfoDto.getContentLength())
                .build();
    }
}

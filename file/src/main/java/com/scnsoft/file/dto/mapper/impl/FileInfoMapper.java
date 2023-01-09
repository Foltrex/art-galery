package com.scnsoft.file.dto.mapper.impl;

import com.scnsoft.file.dto.FileInfoDto;
import com.scnsoft.file.dto.mapper.Mapper;
import com.scnsoft.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FileInfoMapper implements Mapper<FileInfo, FileInfoDto> {

    @Override
    public FileInfoDto mapToDto(FileInfo fileInfo) {
        return FileInfoDto.builder()
                .id(fileInfo.getId())
                .artId(fileInfo.getArtId())
                .mimeType(fileInfo.getMimeType())
                .filename(fileInfo.getFilename())
                .build();
    }

    @Override
    public FileInfo mapToEntity(FileInfoDto fileInfoDto) {
        return FileInfo.builder()
                .id(fileInfoDto.getId())
                .artId(fileInfoDto.getArtId())
                .mimeType(fileInfoDto.getMimeType())
                .filename(fileInfoDto.getFilename())
                .build();
    }

    public Page<FileInfoDto> mapPageToDto(final Page<FileInfo> fileInfoPage) {
        return new PageImpl<>(fileInfoPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), fileInfoPage.getPageable(), fileInfoPage.getTotalElements());

    }
}

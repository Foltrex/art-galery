package com.scnsoft.art.dto.mapper;
import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.FileInfo;
import org.springframework.stereotype.Component;

@Component
public class FileInfoMapper {

    public FileInfoDto mapToDto(FileInfo fileInfo) {
        return FileInfoDto.builder()
                .id(fileInfo.getId())
                .mimeType(fileInfo.getMimeType())
                .contentLength(fileInfo.getContentLength())
                .directory(fileInfo.getDirectory())
                .createdAt(fileInfo.getCreatedAt())
                .cacheControl(fileInfo.getCacheControl())
                .build();
    }

    public FileInfo mapToEntity(FileInfoDto fileInfoDto) {
        return FileInfo.builder()
                .id(fileInfoDto.getId())
                .mimeType(fileInfoDto.getMimeType())
                .contentLength(fileInfoDto.getContentLength())
                .directory(fileInfoDto.getDirectory())
                .createdAt(fileInfoDto.getCreatedAt())
                .cacheControl(fileInfoDto.getCacheControl())
                .build();
    }
}
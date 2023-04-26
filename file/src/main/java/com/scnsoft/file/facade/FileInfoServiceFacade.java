package com.scnsoft.file.facade;

import java.util.UUID;

import com.scnsoft.file.exception.ResourseNotFoundException;
import org.springframework.stereotype.Component;

import com.scnsoft.file.dto.FileInfoDto;
import com.scnsoft.file.dto.mapper.impl.FileInfoMapper;
import com.scnsoft.file.service.FileService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileInfoServiceFacade {
    private final FileInfoMapper fileInfoMapper;
    private final FileService fileService;

    public FileInfoDto findById(UUID id) {
        return fileService.findFileInfoById(id).map(fileInfoMapper::mapToDto)
                .orElseThrow(() -> new ResourseNotFoundException("File not found"));
    }
}

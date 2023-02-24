package com.scnsoft.file.facade;

import java.util.List;
import java.util.UUID;

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

    public List<FileInfoDto> findAllFirstByArtIds(List<UUID> artIds) {
        return fileService.findAllFirstByArtId(artIds)
            .stream()
            .map(fileInfoMapper::mapToDto)
            .toList();
    }

    public List<FileInfoDto> findAllByArtId(UUID artId) {
        return fileService.findAllByArtId(artId)
            .stream()
            .map(fileInfoMapper::mapToDto)
            .toList();
    }
}

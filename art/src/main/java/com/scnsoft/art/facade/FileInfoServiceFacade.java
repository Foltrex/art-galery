package com.scnsoft.art.facade;

import java.util.UUID;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.dto.mapper.FileInfoMapper;
import com.scnsoft.art.service.FileServiceImplFile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class FileInfoServiceFacade {
    private final FileInfoMapper fileInfoMapper;
    private final FileServiceImplFile fileService;

    public FileInfoDto findById(UUID id) {
        return fileService.findFileInfoById(id).map(fileInfoMapper::mapToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
    }
}

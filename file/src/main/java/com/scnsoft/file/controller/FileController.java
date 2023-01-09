package com.scnsoft.file.controller;

import com.scnsoft.file.dto.FileInfoDto;
import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.dto.mapper.impl.FileInfoMapper;
import com.scnsoft.file.service.impl.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;
    private final FileInfoMapper fileInfoMapper;

    @GetMapping(value = "/{id}/data", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getFileStreamById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(new InputStreamResource(fileService.getFileStream(id).getInputStream()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileInfoDto> findFileInfoById(@PathVariable UUID id) {
        return new ResponseEntity<>(fileInfoMapper.mapToDto(fileService.findFileInfoById(id)), HttpStatus.OK);
    }

    @GetMapping("/arts/{artId}")
    public ResponseEntity<Page<FileInfoDto>> findAllFileInfoByArtId(@PathVariable UUID artId, Pageable pageable) {
        return ResponseEntity.ok().body(fileInfoMapper.mapPageToDto(fileService.findAllFileInfoByArtId(artId, pageable)));
    }

    @PostMapping
    public ResponseEntity<FileInfoDto> uploadFile(@RequestBody UploadFileDto uploadFileDto) {
        return ResponseEntity.ok().body(fileInfoMapper.mapToDto(fileService.uploadFile(uploadFileDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFileById(@PathVariable UUID id) {
        fileService.removeFileById(id);
        return ResponseEntity.ok().build();
    }
}

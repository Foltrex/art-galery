package com.scnsoft.file.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.file.dto.FileInfoDto;
import com.scnsoft.file.dto.FileStreamDto;
import com.scnsoft.file.dto.UploadFileDto;
import com.scnsoft.file.dto.mapper.impl.FileInfoMapper;
import com.scnsoft.file.facade.FileInfoServiceFacade;
import com.scnsoft.file.service.impl.FileServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileServiceImpl fileService;
    private final FileInfoMapper fileInfoMapper;
    private final FileInfoServiceFacade fileInfoServiceFacade;

    @GetMapping(value = "/data", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<List<InputStreamResource>> getFileStreamById(@RequestParam List<UUID> ids) {  
        log.info("Ids values: {}", ids);
        List<FileStreamDto> dtos = fileService.getFileStream(ids);
        List<InputStreamResource> resources = dtos
            .stream()
            .map(fileStream -> new InputStreamResource(fileStream.getInputStream()))
            .toList();

        log.info(resources.toString());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileInfoDto> findFileInfoById(@PathVariable UUID id) {
        return new ResponseEntity<>(fileInfoMapper.mapToDto(fileService.findFileInfoById(id)), HttpStatus.OK);
    }

    @GetMapping("/arts/{artId}")
    public ResponseEntity<List<FileInfoDto>> findAllByArtId(@PathVariable UUID  artId) {
        return ResponseEntity.ok(fileInfoServiceFacade.findAllByArtId(artId));
    }

    @GetMapping("/arts/first")
    public ResponseEntity<List<FileInfoDto>> findAllFirstByArtId(@RequestParam List<UUID> artId) {
        log.info(artId.toString());
        return ResponseEntity.ok(fileInfoServiceFacade.findAllFirstByArtIds(artId));
    }

    // @GetMapping("/arts/{artId}")
    // public ResponseEntity<Page<FileInfoDto>> findAllFileInfoByArtId(@PathVariable UUID artId, Pageable pageable) {
    //     return ResponseEntity.ok().body(fileInfoMapper.mapPageToDto(fileService.findAllFileInfoByArtId(artId, pageable)));
    // }

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

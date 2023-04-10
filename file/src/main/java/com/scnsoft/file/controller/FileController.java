package com.scnsoft.file.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.scnsoft.file.entity.FileInfo;
import com.scnsoft.file.exception.ResourseNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.file.dto.FileInfoDto;
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
@Transactional
public class FileController {

    private final FileServiceImpl fileService;
    private final FileInfoMapper fileInfoMapper;
    private final FileInfoServiceFacade fileInfoServiceFacade;

    @GetMapping(value = "/{id}/data", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getFileStreamById(@PathVariable UUID id) {
        FileInfoDto info = fileInfoServiceFacade.findById(id);
        return ResponseEntity
                .ok()
                .contentLength(info.getContentLength())
                .contentType(MediaType.parseMediaType(info.getMimeType()))
                .body(new InputStreamResource(fileService.getFileStream(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileInfoDto> findFileInfoById(@PathVariable UUID id) {
        return fileService.findFileInfoById(id)
                .map(f -> new ResponseEntity<>(fileInfoMapper.mapToDto(f), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //@TODO REMOVE
//    @GetMapping("/arts/{artId}")
//    public ResponseEntity<List<FileInfoDto>> findAllByArtId(@PathVariable UUID artId) {
//        return ResponseEntity.ok(fileInfoServiceFacade.findAllByArtId(artId));
//    }
//
//    @DeleteMapping("/arts/{artId}")
//    public ResponseEntity<Void> deleteByArtId(@PathVariable UUID artId) {
//        fileService.deleteByArtId(artId);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<List<FileInfoDto>> uploadFile(@RequestBody UploadFileDto uploadFileDto) {
        List<FileInfo> fileInfoList = fileService.uploadFile(uploadFileDto);
        List<FileInfoDto> fileInfoDtoList = fileInfoList.stream().map(fileInfoMapper::mapToDto).toList();
        return ResponseEntity.ok().body(fileInfoDtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFileById(@PathVariable UUID id) {
        try {
            fileService.removeFileById(id);
        } catch (ResourseNotFoundException e) {
            log.info("Attempt to delete file which does not exists: " + id);
        }
        return ResponseEntity.ok().build();
    }
}

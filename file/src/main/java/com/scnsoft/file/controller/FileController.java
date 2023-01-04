package com.scnsoft.file.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.file.dto.ArtDto;
import com.scnsoft.file.service.FileService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/files")
@Slf4j
public record FileController(FileService fileService) {
    
    @PostMapping
    public ArtDto save(@RequestBody ArtDto artDto) {
        log.info(artDto.toString());
        return fileService.save(artDto);
    }
}

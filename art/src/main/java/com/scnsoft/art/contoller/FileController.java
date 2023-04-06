package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.EntityFileInfoDto;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<EntityFile>> findAllByEntityId(@RequestParam("entityId") UUID entityId) {
        return ResponseEntity.ok().body(fileService.findAllByEntityId(entityId));
    }

    @PostMapping
    public ResponseEntity<List<EntityFile>> save(@RequestBody UploadEntityFileDto uploadEntityFileDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.uploadFile(uploadEntityFileDto));
    }

}

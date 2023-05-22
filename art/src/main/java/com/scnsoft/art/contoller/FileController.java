package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.dto.mapper.FileInfoMapper;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.service.FileService;
import com.scnsoft.art.service.FileServiceImplFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.scnsoft.art.service.FileServiceImplFile.temp;

@Slf4j
@RestController
@RequestMapping("files")
@RequiredArgsConstructor
@Transactional
public class FileController {

    private final FileService fileService;
    private final FileServiceImplFile fileService1;
    private final FileInfoMapper fileInfoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<FileInfoDto> findFileInfoById(@PathVariable UUID id) {
        return fileService1.findFileInfoById(id)
                .map(f -> new ResponseEntity<>(fileInfoMapper.mapToDto(f), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}/data", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getFileStreamById(@PathVariable UUID id) {
        var data = fileService1.getFileStream(id);
        var info = data.fileInfo();
        var cacheControl = info.getCacheControl();
        return ResponseEntity
                .ok()
                .cacheControl(cacheControl == null || cacheControl == 0
                        ? CacheControl.noCache()
                        : CacheControl.maxAge(cacheControl == -1 ? 31536000L : cacheControl, TimeUnit.SECONDS))
                .contentLength(info.getContentLength())
                .contentType(MediaType.parseMediaType(info.getMimeType()))
                .body(new InputStreamResource(data.inputStream()));
    }

    @PostMapping("/temp")
    public ResponseEntity<FileInfo> uploadFile(@RequestBody FileInfoDto uploadEntityFileDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.uploadTempFile(uploadEntityFileDto));
    }

    @Scheduled(fixedRate = 24L * 60 * 60 * 1000)
    public void cleanupTemp() {
        log.info("Scheduled task - cleanup temp dir");
        fileService1.findFiles(FileServiceImplFile.FileFilter
                .builder()
                .dir(temp + "%")
                .createdAt(new Date(new Date().getTime() - (24L * 3600 * 1000)))
                .build()
        ).forEach(fileService1::deleteFile);
    }
}

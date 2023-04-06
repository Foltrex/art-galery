package com.scnsoft.art.feignclient;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.dto.UploadFileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "file-service", url = "http://localhost:8080/file-service/files")
public interface FileFeignClient {

    @PostMapping("")
    List<FileInfoDto> uploadFile(@RequestBody UploadFileDto uploadFileDto);

    //@TODO REMOVE
    @DeleteMapping("/arts/{artId}")
    ResponseEntity<Void> deleteByArtId(@PathVariable("artId") UUID artId);
}

package com.scnsoft.art.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.scnsoft.art.dto.UploadArtDto;

@FeignClient(value = "file-service", url = "http://localhost:8080/file-service")
public interface FileFeignClient {
    @PostMapping
    UploadArtDto save(@RequestBody UploadArtDto uploadArtDto);
}

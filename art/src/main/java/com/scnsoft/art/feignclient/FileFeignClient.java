package com.scnsoft.art.feignclient;

import com.scnsoft.art.dto.ArtDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-service", url = "http://localhost:8080/file-service/files")
public interface FileFeignClient {
    @PostMapping
    ArtDto save(@RequestBody ArtDto artDto);
}

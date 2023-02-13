package com.scnsoft.art.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "file-service", url="http://localhost:8080/file-service/files")
public interface FileFeignClient {
    @DeleteMapping("/arts/{artId}")
    ResponseEntity<Void> deleteByArtId(@PathVariable("artId") UUID artId);
}

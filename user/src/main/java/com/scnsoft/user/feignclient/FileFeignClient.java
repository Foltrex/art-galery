package com.scnsoft.user.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.UploadFileDto;

@FeignClient(value = "file-service", url = "http://localhost:8082/files")
public interface FileFeignClient {

    @PostMapping("")
    FileInfoDto uploadFile(@RequestBody UploadFileDto uploadFileDto);

    // @PostMapping("/{id}")
    // void removeFile(@PathVariable String id);

}

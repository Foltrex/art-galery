package com.scnsoft.user.feignclient;

import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.UploadFileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-service", url = "http://localhost:8082/files")
public interface FileFeignClient {

    @PostMapping("")
    FileInfoDto uploadFile(@RequestBody UploadFileDto uploadFileDto);

    @DeleteMapping("/{id}")
    void removeFile(@PathVariable("id") String id);

}

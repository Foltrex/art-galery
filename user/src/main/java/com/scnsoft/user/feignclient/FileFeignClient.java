package com.scnsoft.user.feignclient;

import com.scnsoft.user.dto.FileInfoDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.payload.EmailMessagePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-service", url = "http://localhost:8082/files")
public interface FileFeignClient {

    @PostMapping("")
    FileInfoDto uploadFile(@RequestBody UploadFileDto uploadFileDto);

}

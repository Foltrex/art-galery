package com.scnsoft.file.feignclient;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.scnsoft.file.dto.AccountDto;
import com.scnsoft.file.payload.AuthToken;

@FeignClient(value = "user-service-accounts", url = "http://localhost:8083/auth")
public interface AuthFeignClient {

    @PostMapping("/register")
    public ResponseEntity<AuthToken> register(
        @Valid @RequestBody AccountDto request
    );
}

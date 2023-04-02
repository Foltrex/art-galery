package com.scnsoft.art.feignclient;

import com.scnsoft.art.contoller.AuthController;
import com.scnsoft.art.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "user-service-auth", url = "http://localhost:8083/auth")
public interface AuthFeignClient {

    @PostMapping("register")
    ResponseEntity<AuthController.AuthToken> register(@RequestBody AccountDto email);


    @GetMapping("/{id}")
    AccountDto findById(@PathVariable("id") UUID id);
}

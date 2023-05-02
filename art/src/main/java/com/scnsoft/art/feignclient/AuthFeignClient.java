package com.scnsoft.art.feignclient;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.scnsoft.art.contoller.AuthController;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.payload.AuthToken;
import com.scnsoft.art.payload.LoginRequest;

@FeignClient(value = "user-service-auth", url = "http://localhost:8083/auth")
public interface AuthFeignClient {

    @PostMapping("register")
    ResponseEntity<AuthController.AuthToken> register(@RequestBody AccountDto email);


    @PostMapping("/register-user")
    ResponseEntity<AccountDto> registerUser(
        @Valid @RequestBody AccountDto registeringUser
    );

    @PostMapping("/login")
    AuthToken login(@RequestBody LoginRequest request);


    @GetMapping("/{id}")
    AccountDto findById(@PathVariable("id") UUID id);
}

package com.scnsoft.user.controller;

import com.scnsoft.user.controller.dto.request.LoginRequestDto;
import com.scnsoft.user.controller.dto.request.RegisterRequestDto;
import com.scnsoft.user.controller.dto.response.AccountResponseDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<AccountResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return new ResponseEntity<>(accountService.register(registerRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(accountService.login(loginRequestDto), HttpStatus.OK);
    }
}

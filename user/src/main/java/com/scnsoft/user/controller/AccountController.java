package com.scnsoft.user.controller;

import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.AccountResponseDto;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.util.ArtistFeignClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private final ArtistFeignClientUtil artFeignClientUtil;

    @PostMapping("/register")
    @PreAuthorize("@accountSecurityHandler.isHasRegisterAccess(#registerRequestDto.accountType)")
    public ResponseEntity<AccountResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return new ResponseEntity<>(accountService.register(registerRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(accountService.login(loginRequestDto), HttpStatus.OK);
    }


    ////////////////////////////////////// FOR TESTS ////////////////////////////////

    @GetMapping("/1")
    @PreAuthorize("permitAll()")
    public String test1() {
        log.info(artFeignClientUtil.test().getBody());
        return "PERMIT ALL";
    }

    @GetMapping("/2")
    @PreAuthorize("hasRole('USER')")
    public String test2() {
        return "PERMIT USER";
    }

    @GetMapping("/3")
    @PreAuthorize("hasRole('ADMIN')")
    public String test3() {
        return "PERMIT ADMIN";
    }

    @GetMapping("/4")
    @PreAuthorize("@accountSecurityHandler.isHasType('ARTIST')")
    public String test4() {
        return "PERMIT ARTIST";
    }
}

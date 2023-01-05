package com.scnsoft.user.controller;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.AuthTokenDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRepresentativeRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.dto.mapper.impl.AccountMapper;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    /**
     * Account service for working with account's login, register, etc.
     * {@link com.scnsoft.user.service.impl.AccountServiceImpl}
     */
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/byEmail/{email}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AccountDto> findByEmail(@PathVariable String email) {
        return new ResponseEntity<>(accountMapper.mapToDto(accountService.findByEmail(email)), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthTokenDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return new ResponseEntity<>(accountService.register(registerRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/register-representative-to-organization")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepresentativeDto> registerRepresentative(
            @RequestBody RegisterRepresentativeRequestDto registerRepresentativeRequestDto) {
        return new ResponseEntity<>(
                accountService.registerRepresentativeToOrganization(registerRepresentativeRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(accountService.login(loginRequestDto), HttpStatus.OK);
    }

    ////////////////////////////////////// FOR TESTS ////////////////////////////////

    @GetMapping("/1")
    @PreAuthorize("permitAll()")
    public String test1() {
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
    @PreAuthorize("@accountSecurityHandler.isHasType('ARTIST, SYSTEM')")
    public String test4() {
        return "PERMIT ARTIST";
    }
}

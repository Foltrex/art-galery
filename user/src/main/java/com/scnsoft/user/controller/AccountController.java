package com.scnsoft.user.controller;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.dto.mapper.impl.AccountMapper;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;
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

import javax.validation.Valid;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    /**
     * Account service for working with account's business logic.
     * {@link com.scnsoft.user.service.impl.AccountServiceImpl}
     */
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/byEmail/{email}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AccountDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountMapper.mapToDto(accountService.findByEmail(email)));
    }

    @PostMapping("/register-representative")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepresentativeDto> registerRepresentative(
            @Valid @RequestBody RegisterRepresentativeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.registerRepresentative(request));
    }

    ////////////////////////////////////// FOR TESTS ////////////////////////////////

    @GetMapping("/1")
    @PreAuthorize("permitAll()")
    public String test1() {
        return "PERMIT ALL";
    }

    @PostMapping("/post")
    @PreAuthorize("permitAll()")
    public String post() {
        return "POST REQUEST";
    }

    @PostMapping("/post1")
    @PreAuthorize("permitAll()")
    public String post(@RequestBody String word) {
        return "POST REQUEST: " + word;
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

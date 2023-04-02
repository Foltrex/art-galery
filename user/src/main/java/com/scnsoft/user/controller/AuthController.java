package com.scnsoft.user.controller;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.PasswordRecoveryRequest;
import com.scnsoft.user.payload.SendEmailMessageRequest;
import com.scnsoft.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthToken> register(@Valid @RequestBody AccountDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthToken> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // @PostMapping("/register-representative")
    // // @PreAuthorize("isAuthenticated()")
    // public ResponseEntity<RepresentativeDto> registerRepresentative(
    //         @Valid @RequestBody RegisterRepresentativeRequest request) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerRepresentative(request));
    // }

    @PostMapping("/password-recovery-code")
    @PreAuthorize("permitAll()")
    public void sendPasswordRecoveryCode(@Valid @RequestBody SendEmailMessageRequest sendEmailMessageRequest) {
        authService.sendPasswordRecoveryCode(sendEmailMessageRequest.getReceiver());
    }

    @PostMapping("/password-recovery")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> passwordRecovery(@Valid @RequestBody PasswordRecoveryRequest request) {
        authService.passwordRecovery(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout() {
        authService.logout();
    }

}

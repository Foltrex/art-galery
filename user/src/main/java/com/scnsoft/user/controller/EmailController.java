package com.scnsoft.user.controller;

import com.scnsoft.user.dto.RepresentativeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {

    @PostMapping("/password-recovery-code")
    @PreAuthorize("permitAll()")
    public ResponseEntity<RepresentativeDto> sendPasswordRecoveryCode() {
        return null;
    }

}

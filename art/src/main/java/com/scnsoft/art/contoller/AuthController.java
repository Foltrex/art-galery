package com.scnsoft.art.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AuthToken;
import com.scnsoft.art.dto.ErrorDto;
import com.scnsoft.art.dto.LoginRequest;
import com.scnsoft.art.dto.MetaDataDto;
import com.scnsoft.art.dto.PasswordRecoveryRequest;
import com.scnsoft.art.dto.SendEmailMessageRequest;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.MetadataValidationService;
import com.scnsoft.art.service.OrganizationService;
import com.scnsoft.art.service.user.AuthServiceImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Transactional
public class AuthController {

    private final ObjectMapper objectMapper;
    private final OrganizationService organizationService;
    private final FacilityService facilityService;
    private final MetadataValidationService metadataValidationService;
    private final AuthServiceImpl authService;


    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthToken> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout() {
        authService.logout();
    }


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


    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthToken> register(@Valid @RequestBody AccountDto accountDto) {
        accountDto.setId(UUID.randomUUID());
        UUID allowedOrgId = null;
        switch (accountDto.getAccountType()) {
            case REPRESENTATIVE -> {
                Organization organization = organizationService.save(Organization.builder()
                        .id(UUID.randomUUID())
                        .status(Organization.Status.ACTIVE)
                        .name(accountDto.getFirstName() + " " + accountDto.getLastName() + " org")
                        .build());
                allowedOrgId = organization.getId();
                facilityService.save(Facility.builder()
                        .organization(organization)
                        .isActive(true)
                        .name(accountDto.getFirstName() + " " + accountDto.getLastName() + " facility")
                        .build(), new ArrayList<>());
                accountDto.getMetadata()
                        .add(MetaDataDto.builder()
                                .key("organizationId")
                                .value(organization.getId().toString())
                                .build());
                accountDto.getMetadata()
                        .add(MetaDataDto.builder()
                                .key("organizationRole")
                                .value(Organization.Role.CREATOR.toString())
                                .build());
            }
        }

        if(!metadataValidationService.validate(accountDto, allowedOrgId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Metadata set is invalid");
        }

        try {
            return ResponseEntity.ok(authService.register(accountDto));
        } catch (FeignException e) {
            String message;
            try {
                ErrorDto errorDto = objectMapper.readValue(e.contentUTF8(), ErrorDto.class);
                message = errorDto.getMessage();
            } catch (IOException ex) {
                message = "Something went wrong";
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<AccountDto> registerUser(
        @Valid @RequestBody AccountDto registeringUser
    ) {
        if(!metadataValidationService.validate(registeringUser, null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Metadata set is invalid");
        }

        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(authService.registerUser(registeringUser));
        } catch (FeignException e) {
            String message;
            try {
                ErrorDto errorDto = objectMapper.readValue(
                    e.contentUTF8(), 
                    ErrorDto.class
                );
                
                message = errorDto.getMessage();
            } catch (JsonProcessingException ex) {
                message = "Something went wrong";
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}

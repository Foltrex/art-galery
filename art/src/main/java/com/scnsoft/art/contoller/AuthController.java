package com.scnsoft.art.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.ErrorDto;
import com.scnsoft.art.dto.MetaData;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.feignclient.AuthFeignClient;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.OrganizationService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.UUID;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Transactional
public class AuthController {

    private final ObjectMapper objectMapper;
    private final AuthFeignClient authFeignClient;
    private final OrganizationService organizationService;
    private final FacilityService facilityService;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthToken {
        private String token;
        private String type;
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthToken> register(@Valid @RequestBody AccountDto accountDto) {
        accountDto.setId(UUID.randomUUID());

        switch (accountDto.getAccountType()) {
            case REPRESENTATIVE -> {
                Organization organization = organizationService.save(Organization.builder()
                        .id(UUID.randomUUID())
                        .status(Organization.Status.ACTIVE)
                        .name(accountDto.getFirstName() + " " + accountDto.getLastName() + " org")
                        .build());
                facilityService.save(Facility.builder()
                        .organization(organization)
                        .isActive(true)
                        .name(accountDto.getFirstName() + " " + accountDto.getLastName() + " facility")
                        .build());

                accountDto.getMetadata()
                        .add(MetaData.builder()
                                .key("organizationId")
                                .value(organization.getId().toString())
                                .build());
                accountDto.getMetadata()
                        .add(MetaData.builder()
                                .key("organizationRole")
                                .value(Organization.Role.CREATOR.toString())
                                .build());
            }
        }
        try {
            return authFeignClient.register(accountDto);
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
    public ResponseEntity<AccountDto> registerUser(@Valid @RequestBody AccountDto registeringUser) {
        try {
            return authFeignClient.registerUser(registeringUser);
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

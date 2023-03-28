package com.scnsoft.user.controller;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.mapper.impl.AccountMapper;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findById(@PathVariable UUID id) {
        log.info(accountService.findById(id).toString());
        return ResponseEntity.ok(accountMapper.mapToDto(accountService.findById(id)));
    }

    @GetMapping("/byEmail/{email}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AccountDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountMapper.mapToDto(accountService.findByEmail(email)));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("isAuthenticated() and #id == authentication.principal.id")
    public void updatePassword(@PathVariable UUID id, @Valid @RequestBody UpdatePasswordRequest request) {
        accountService.updatePasswordById(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == authentication.principal.id")
    public void deleteById(@PathVariable UUID id) {
        accountService.deleteById(id);
    }
}

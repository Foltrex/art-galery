package com.scnsoft.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.scnsoft.user.dto.AccountFilter;
import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.MetadataId;
import liquibase.pro.packaged.M;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.UploadFileDto;
import com.scnsoft.user.dto.mapper.AccountMapper;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountController {

    /**
     * Account service for working with account's business logic.
     * {@link com.scnsoft.user.service.impl.AccountServiceImpl}
     */
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<AccountDto> findAll(
        Pageable pageable,
        AccountFilter accountFilter
    ) {
        return accountService.findAll(pageable, accountFilter).map(accountMapper::mapToDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AccountDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountMapper.mapToDto(accountService.findById(id)));
    }

    //@Todo make facade later
    @PutMapping("/{id}")
    @PreAuthorize("@accountServiceImpl.isEditingUser(authentication.principal.id, #id)")
    public ResponseEntity<AccountDto> updateById(@PathVariable UUID id, @Valid @RequestBody AccountDto request) {
        List<Metadata> metadataList = new ArrayList<>(Optional.ofNullable(request.getMetadata())
                .orElse(new ArrayList<>())
                .stream()
                .map(m -> {
                    Metadata result = new Metadata();
                    result.setValue(m.getValue());
                    MetadataId metadataId = new MetadataId();
                    metadataId.setAccountId(id);
                    metadataId.setKey(m.getKey());
                    result.setMetadataId(metadataId);
                    return result;
                }).toList());//toList return unmodifiable collection, need to wrap into array list
        Account account = accountMapper.mapToEntity(request);
        account.setMetadata(metadataList);
        return ResponseEntity.ok(accountMapper.mapToDto(
                accountService.updateById(id, account)));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("@accountServiceImpl.isEditingUser(authentication.principal.id, #id)")
    public void updatePassword(@PathVariable UUID id, @Valid @RequestBody UpdatePasswordRequest request) {
        accountService.updatePasswordById(id, request);
    }

    @PatchMapping("/{id}/account-image")
    @PreAuthorize("@accountServiceImpl.isEditingUser(authentication.principal.id, #id)")
    public void updateAccountImage(@PathVariable UUID id, @Valid @RequestBody UploadFileDto request) {
        accountService.updateImageById(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accountServiceImpl.isEditingUser(authentication.principal.id, #id)")
    public void deleteById(@PathVariable UUID id) {
        accountService.deleteById(id);
    }
}

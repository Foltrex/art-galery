package com.scnsoft.art.contoller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AccountFilter;
import com.scnsoft.art.dto.MetaDataDto;
import com.scnsoft.art.dto.UpdatePasswordRequest;
import com.scnsoft.art.dto.mapper.AccountMapper;
import com.scnsoft.art.dto.mapper.MetadataMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.MetadataId;
import com.scnsoft.art.service.user.AccountServiceImpl;
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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountController {


    private final AccountServiceImpl accountService;
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
        Set<Metadata> metadataList = Optional.ofNullable(request.getMetadata())
                .orElse(new HashSet<>())
                .stream()
                .map(m -> {
                    Metadata result = new Metadata();
                    result.setValue(m.getValue());
                    MetadataId metadataId = new MetadataId();
                    metadataId.setAccountId(id);
                    metadataId.setKey(m.getKey());
                    result.setMetadataId(metadataId);
                    return result;
                }).collect(Collectors.toSet());
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

    @DeleteMapping("/{id}")
    @PreAuthorize("@accountServiceImpl.isEditingUser(authentication.principal.id, #id)")
    public void deleteById(@PathVariable UUID id) {
        accountService.deleteById(id);
    }
}

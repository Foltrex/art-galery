package com.scnsoft.art.contoller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.facade.RepresentativeServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("representatives")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeServiceFacade representativeServiceFacade;

    @GetMapping
    public ResponseEntity<Page<RepresentativeDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(representativeServiceFacade.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentativeDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(representativeServiceFacade.findById(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<RepresentativeDto>> findAllByAccountId(@PathVariable UUID accountId, Pageable pageable) {
        return ResponseEntity.ok(representativeServiceFacade.findAllByAccountId(accountId, pageable));
    }

    @GetMapping("/accounts/{accountId}")
    public RepresentativeDto findByAccountId(@PathVariable UUID accountId) {
        return representativeServiceFacade.findByAccountId(accountId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepresentativeDto> save(@RequestBody RepresentativeDto representativeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(representativeServiceFacade.save(representativeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentativeDto> update(@PathVariable UUID id, @RequestBody RepresentativeDto representativeDto) {
        return ResponseEntity.ok(representativeServiceFacade.updateById(id, representativeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(representativeServiceFacade.deleteById(id));
    }

    @DeleteMapping("/accounts/{accountId}")
    @PreAuthorize("isAuthenticated() and #accountId == authentication.principal.id")
    public ResponseEntity<Void> deleteByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(representativeServiceFacade.deleteByAccountId(accountId));
    }

}

package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.facade.OrganizationServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("organizations")
@RequiredArgsConstructor
@Transactional
public class OrganizationController {

    private final OrganizationServiceFacade organizationServiceFacade;

    @GetMapping
    public Page<OrganizationDto> findAll(
            Pageable pageable,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String status
    ) {
        return organizationServiceFacade.findAll(
                pageable,
                name,
                status
        );
    }

    @GetMapping("/list")
    List<OrganizationDto> findAll() {
        return organizationServiceFacade.findAll();
    }

    @GetMapping("/name")
    OrganizationDto findByName(@RequestParam String name) {
        return organizationServiceFacade.findByName(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationServiceFacade.findById(id));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<OrganizationDto> findByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(organizationServiceFacade.findByAccountId(accountId));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> save(@RequestBody OrganizationDto organizationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationServiceFacade.save(organizationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> update(@PathVariable UUID id, @RequestBody OrganizationDto organizationDto) {
        return ResponseEntity.ok(organizationServiceFacade.updateById(id, organizationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationServiceFacade.deleteById(id));
    }

}

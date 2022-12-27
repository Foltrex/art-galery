package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("organizations")
public record OrganizationController(OrganizationService organizationService) {

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> findAll() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> save(@RequestBody OrganizationDto OrganizationDto) {
        return new ResponseEntity<>(organizationService.save(OrganizationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> update(@PathVariable UUID id, @RequestBody OrganizationDto OrganizationDto) {
        return ResponseEntity.ok(organizationService.update(id, OrganizationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

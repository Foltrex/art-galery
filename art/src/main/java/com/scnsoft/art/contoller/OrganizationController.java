package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.mapper.impl.OrganizationMapper;
import com.scnsoft.art.entity.Organization;
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
public record OrganizationController(
    OrganizationService organizationService, 
    OrganizationMapper organizationMapper
) {

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> findAll() {
        List<OrganizationDto> organizationDtos = organizationService.findAll()
            .stream()
            .map(organizationMapper::mapToDto)
            .toList();

        return ResponseEntity.ok(organizationDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationMapper.mapToDto(organizationService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> save(@RequestBody OrganizationDto organizationDto) {
        Organization organization = organizationService.save(organizationMapper.mapToEntity(organizationDto));
        return new ResponseEntity<>(organizationMapper.mapToDto(organization), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> update(@PathVariable UUID id, @RequestBody OrganizationDto organizationDto) {
        Organization organization = organizationService.update(id, organizationMapper.mapToEntity(organizationDto));
        return ResponseEntity.ok(organizationMapper.mapToDto(organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

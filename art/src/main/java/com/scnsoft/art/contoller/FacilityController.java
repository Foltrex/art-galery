package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.impl.FacilityMapper;
import com.scnsoft.art.service.FacilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.UUID;

@RestController
@RequestMapping("facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityServiceImpl facilityService;
    private final FacilityMapper facilityMapper;

    @GetMapping
    public ResponseEntity<Page<FacilityDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(facilityMapper.mapPageToDto(facilityService.findAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(facilityMapper.mapToDto(facilityService.findById(id)));
    }

    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<Page<FacilityDto>> findAllByOrganizationId(@PathVariable UUID organizationId,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(facilityMapper.mapPageToDto(
                facilityService.findAllByOrganizationId(organizationId, pageable)));
    }


    @PostMapping
    public ResponseEntity<FacilityDto> save(@RequestBody FacilityDto facilityDto) {
        return new ResponseEntity<>(facilityService.save(facilityDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityDto> update(@PathVariable UUID id, @RequestBody FacilityDto facilityDto) {
        return ResponseEntity.ok(facilityService.update(id, facilityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        facilityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

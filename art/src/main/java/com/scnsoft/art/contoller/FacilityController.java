package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.facade.FacilityServiceFacade;
import com.scnsoft.art.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("facilities")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FacilityController {

    private final FacilityServiceFacade facilityServiceFacade;

    @GetMapping
    public ResponseEntity<Page<FacilityDto>> findAll(
        Pageable pageable,
        FacilityFilter filter
    ) {
        log.info("Current account id: {}", SecurityUtil.getCurrentAccountId());
        return ResponseEntity.ok(facilityServiceFacade.findAll(pageable, filter));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FacilityDto>> findAll(FacilityFilter filter) {
        return ResponseEntity.ok(facilityServiceFacade.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(facilityServiceFacade.findById(id));
    }

    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<Page<FacilityDto>> findAllByOrganizationId(@PathVariable UUID organizationId,
            Pageable pageable) {
        return ResponseEntity.ok(facilityServiceFacade.findAllByOrganizationId(organizationId, pageable));
    }

    @GetMapping("accounts/{accountId}")
    public ResponseEntity<FacilityDto> findByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(facilityServiceFacade.findByAccountId(accountId));
    }

    @GetMapping("page/accounts/{accountId}")
    public ResponseEntity<Page<FacilityDto>> findAllByAccountId(@PathVariable UUID accountId, Pageable pageable) {
        return ResponseEntity.ok(facilityServiceFacade.findAllByAccountId(accountId, pageable));
    }

    @GetMapping("list/accounts/{accountId}")
    public ResponseEntity<List<FacilityDto>> findAllByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(facilityServiceFacade.findAllByAccountId(accountId));
    }

    @PostMapping
    public ResponseEntity<FacilityDto> save(@RequestBody FacilityDto facilityDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityServiceFacade.save(facilityDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityDto> update(@PathVariable UUID id, @RequestBody FacilityDto facilityDto) {
        return ResponseEntity.ok(facilityServiceFacade.updateById(id, facilityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(facilityServiceFacade.deleteById(id));
    }
}

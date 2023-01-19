package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.dto.mapper.impl.RepresentativeMapper;
import com.scnsoft.art.facade.RepresentativeServiceFacade;
import com.scnsoft.art.service.RepresentativeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.UUID;

@RestController
@RequestMapping("representatives")
@RequiredArgsConstructor
@Slf4j
public class RepresentativeController {

    private final RepresentativeServiceFacade representativeServiceFacade;

    @GetMapping("/end-point")
    public String test() {
        return "end-point is working";
    }

    @GetMapping
    public ResponseEntity<Page<RepresentativeDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(representativeServiceFacade.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentativeDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(representativeServiceFacade.findById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepresentativeDto> save(@RequestBody RepresentativeDto representativeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(representativeServiceFacade.create(representativeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentativeDto> update(@PathVariable UUID id,
                                                    @RequestBody RepresentativeDto representativeDto) {
        return ResponseEntity.ok(representativeServiceFacade.update(id, representativeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(representativeServiceFacade.delete(id));
    }

}

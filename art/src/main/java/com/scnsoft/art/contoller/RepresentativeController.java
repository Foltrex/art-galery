package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.RepresentativeService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("representatives")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;

    @GetMapping
    public ResponseEntity<List<RepresentativeDto>> findAll() {
        return ResponseEntity.ok(representativeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentativeDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(representativeService.findById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepresentativeDto> save(@RequestBody RepresentativeDto RepresentativeDto) {
        return new ResponseEntity<>(representativeService.save(RepresentativeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentativeDto> update(@PathVariable UUID id, @RequestBody RepresentativeDto RepresentativeDto) {
        return ResponseEntity.ok(representativeService.update(id, RepresentativeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        representativeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

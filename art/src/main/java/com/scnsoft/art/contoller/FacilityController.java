package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
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

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.service.FacilityService;

@RestController
@RequestMapping("api/v1/facilities")
public record FacilityController(FacilityService facilityService) {
    @GetMapping
    public ResponseEntity<List<Facility>> findAll() {
        return ResponseEntity.ok(facilityService.findAll());
    }

    @PostMapping
    public ResponseEntity<Facility> save(@RequestBody Facility facility) {
        return new ResponseEntity<>(facilityService.save(facility), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> update(@PathVariable UUID id, @RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.update(id, facility));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        facilityService.deleteById(null);
        return ResponseEntity.noContent().build();
    }
}

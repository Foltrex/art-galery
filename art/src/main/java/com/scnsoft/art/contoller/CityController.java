package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

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

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.service.CityService;

@RestController
@RequestMapping("api/v1/cities")
public record CityController(CityService cityService) {
    
    @GetMapping
    public ResponseEntity<List<CityDto>> findAll() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @PostMapping
    public ResponseEntity<CityDto> save(@RequestBody CityDto cityDto) {
        return new ResponseEntity<>(cityService.save(cityDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> update(@PathVariable UUID id, @RequestBody CityDto cityDto) {
        return ResponseEntity.ok(cityService.update(id, cityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        cityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

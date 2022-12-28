package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.service.ArtistService;
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
@RequestMapping("artists")
public record ArtistController(ArtistService artistService) {

    @GetMapping
    public ResponseEntity<List<ArtistDto>> findAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artistService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> save(@RequestBody ArtistDto artistDto) {
        return new ResponseEntity<>(artistService.save(artistDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> update(@PathVariable UUID id, @RequestBody ArtistDto artistDto) {
        return ResponseEntity.ok(artistService.update(id, artistDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

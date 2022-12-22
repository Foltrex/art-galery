package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

import com.scnsoft.art.entity.Artist;
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

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.service.CityService;

@RestController
@RequestMapping("api/v1/cities")
public record ArtistController(ArtistService artistService) {

    @GetMapping
    public ResponseEntity<List<Artist>> findAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @PostMapping
    public ResponseEntity<Artist> save(@RequestBody Artist artist) {
        return new ResponseEntity<>(artistService.save(artist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable UUID id, @RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.update(id, artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

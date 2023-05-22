package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.facade.ArtServiceFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
@Transactional
public class ArtController {
    private final ArtServiceFacade artServiceFacade;

    @GetMapping
    public Page<ArtDto> findAll(Pageable pageable, ArtFilter artFilter) {
        return artServiceFacade.findAll(pageable, artFilter);
    }

    @PostMapping
    public ResponseEntity<ArtDto> save(@RequestBody ArtDto artDto) {
        return ResponseEntity.ok(artServiceFacade.save(artDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artServiceFacade.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        // fileFeignClient.deleteByArtId(id);
        artServiceFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

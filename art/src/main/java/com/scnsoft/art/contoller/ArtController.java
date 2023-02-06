package com.scnsoft.art.contoller;

import java.util.UUID;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.service.impl.ArtServiceImpl;

import com.scnsoft.art.facade.ArtServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
public class ArtController {
    private final ArtServiceFacade artServiceFacade;

    @PostMapping
    public ResponseEntity<ArtDto> save(@RequestBody ArtDto artDto) {
        return ResponseEntity.ok(artServiceFacade.save(artDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artServiceFacade.findById(id));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<List<ArtDto>> findAllByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(artServiceFacade.findAllByAccountId(accountId));
    }
}

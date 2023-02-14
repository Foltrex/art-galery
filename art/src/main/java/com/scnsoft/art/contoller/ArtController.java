package com.scnsoft.art.contoller;

import java.util.UUID;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.service.impl.ArtServiceImpl;

import com.scnsoft.art.facade.ArtServiceFacade;
import com.scnsoft.art.feignclient.FileFeignClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
public class ArtController {
    private final ArtServiceFacade artServiceFacade;
    private final FileFeignClient fileFeignClient;

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
        fileFeignClient.deleteByArtId(id);
        artServiceFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Page<ArtDto>> findAllByAccountIdAndSearchText(@PathVariable UUID accountId, Pageable pageable, @RequestParam(defaultValue = Strings.EMPTY) String name) {
        return ResponseEntity.ok(artServiceFacade.findAllByAccountIdAndName(accountId, pageable, name));
    }
}

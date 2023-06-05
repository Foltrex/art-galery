package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.SupportDto;
import com.scnsoft.art.dto.SupportFilter;
import com.scnsoft.art.dto.SupportThreadDto;
import com.scnsoft.art.facade.SupportFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("support")
@RestController
@RequiredArgsConstructor
public class SupportController {

    private final SupportFacade supportFacade;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<SupportDto> getAll(SupportFilter filter, Sort sort) {
        return supportFacade.findAll(filter, sort);
    }

    @GetMapping("/thread")
    public Page<SupportThreadDto> findThreads(SupportThreadFilter filter, Pageable pageable) {
        return supportFacade.findThreads(filter, pageable);
    }
    @GetMapping("/thread/{id}")
    public ResponseEntity<SupportThreadDto> findThreads(@PathVariable UUID id) {
        return supportFacade.findThread(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/thread")
    public ResponseEntity<SupportThreadDto> createThread(@RequestBody SupportThreadDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supportFacade.createThread(dto));
    }
    @PutMapping("/thread/{id}")
    public ResponseEntity<SupportThreadDto> createThread(@RequestBody SupportThreadDto dto, @PathVariable UUID id) {
        dto.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(supportFacade.updateThread(dto));
    }

    @PostMapping
    public ResponseEntity<SupportDto> createPost(@RequestBody SupportDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supportFacade.createPost(dto));
    }

    @DeleteMapping("/thread/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        supportFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count")
    public Map<String, Integer> getCount(@RequestParam("accountId") UUID id) {
        return supportFacade.getCount(id);
    }
}

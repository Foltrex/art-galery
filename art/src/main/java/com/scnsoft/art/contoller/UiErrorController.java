package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.UiError;
import com.scnsoft.art.repository.UiErrorRepository;
import com.scnsoft.art.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

@Transactional
@RestController
@RequestMapping("errors")
@RequiredArgsConstructor
public class UiErrorController {

    private final UiErrorRepository uiErrorRepository;

    @GetMapping
    public Page<UiError> getAll(Pageable pageable, UiError.UiErrorStatus status) {
        return uiErrorRepository.findAll((r, q, cb) -> {
            Predicate out = cb.and();
            if(status != null) {
                out = cb.and(out, cb.equal(r.get(UiError.Fields.status), status));
            }
            return out;
        }, pageable);
    }

    @PostMapping
    public ResponseEntity<UiError> save(@RequestBody UiError error) {
        error.setCreatedAt(LocalDateTime.now());
        error.setUserId(SecurityUtil.getCurrentAccountId());
        uiErrorRepository.save(error);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    //@todo add SYSTEM_USER check
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UiError> update(@PathVariable Integer id, @RequestBody UiError error) {
        return uiErrorRepository.findById(id)
                .map((existing) -> {
                    existing.setStatus(error.getStatus());
                    uiErrorRepository.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    //@todo add SYSTEM_USER check
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UiError> delete(@PathVariable Integer id) {
        uiErrorRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

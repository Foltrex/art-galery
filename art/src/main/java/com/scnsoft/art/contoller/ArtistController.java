package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.facade.ArtistServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.UUID;

@RestController
@RequestMapping("artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistServiceFacade artistServiceFacade;

    @GetMapping
    public ResponseEntity<Page<ArtistDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(artistServiceFacade.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(artistServiceFacade.findById(id));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ArtistDto> findByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(artistServiceFacade.findByAccountId(accountId));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> save(@RequestBody ArtistDto artistDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistServiceFacade.save(artistDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> update(@PathVariable UUID id, @RequestBody ArtistDto artistDto) {
        return ResponseEntity.ok(artistServiceFacade.updateById(id, artistDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(artistServiceFacade.deleteById(id));
    }

    ////////////////////////////////////// FOR TESTS ////////////////////////////////

    @GetMapping("/1")
    @PreAuthorize("permitAll()")
    public String test1() {
        return "PERMIT ALL";
    }


    @GetMapping("/2")
    @PreAuthorize("hasRole('USER')")
    public String test2() {
        return "PERMIT USER";
    }

    @GetMapping("/3")
    @PreAuthorize("hasRole('ADMIN')")
    public String test3() {
        return "PERMIT ADMIN";
    }

//    @GetMapping("/4")
//    @PreAuthorize("@accountSecurityHandler.isHasType('ARTIST, SYSTEM')")
//    public String test4() {
//        return "PERMIT ARTIST";
//    }

}

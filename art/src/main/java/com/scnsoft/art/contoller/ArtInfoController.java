package com.scnsoft.art.contoller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.facade.ArtInfoServiceFacade;

@RestController
@RequestMapping("/art-infos")
public record ArtInfoController(
    ArtInfoServiceFacade artInfoServiceFacade
) {

    @GetMapping("/art/{artId}")
    public ResponseEntity<ArtInfoDto> findByAccountId(UUID artId) {
        return ResponseEntity.ok(artInfoServiceFacade.findByArtId(artId));
    }
}

package com.scnsoft.art.contoller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.facade.ArtInfoServiceFacade;

@RestController
@RequestMapping("/art-infos")
public record ArtInfoController(
    ArtInfoServiceFacade artInfoServiceFacade
) {
    @GetMapping("/arts/{artId}")
    public List<ArtInfoDto> findAllByArtId(@PathVariable UUID artId) {
        return artInfoServiceFacade.findByArtId(artId);
    }

    @GetMapping("/last/arts/{artId}")
    public ResponseEntity<ArtInfoDto> findLastByArtId(@PathVariable UUID artId) {
        Optional<ArtInfoDto> optionalArtInfo = artInfoServiceFacade.findLastByArtId(artId);
        return optionalArtInfo.isPresent()
            ? ResponseEntity.ok(optionalArtInfo.get())
            : ResponseEntity.ok().build();
    }
}

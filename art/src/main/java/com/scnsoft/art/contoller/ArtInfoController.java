package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.facade.ArtInfoServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/art-infos")
@RequiredArgsConstructor
@Transactional
public class ArtInfoController {

    private final ArtInfoServiceFacade artInfoServiceFacade;

    @GetMapping("/arts/{artId}")
    public List<ArtInfoDto> findAllByArtId(@PathVariable UUID artId) {
        return artInfoServiceFacade.findByArtId(artId);
    }

}

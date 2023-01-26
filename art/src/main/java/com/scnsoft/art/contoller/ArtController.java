package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.service.impl.ArtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
public class ArtController {
    private final ArtServiceImpl artService;

    @PostMapping
    public ArtDto save(@RequestBody ArtDto artDto) {
        return artService.save(artDto);
    }
}

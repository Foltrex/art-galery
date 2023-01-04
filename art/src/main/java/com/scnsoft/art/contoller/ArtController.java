package com.scnsoft.art.contoller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.service.ArtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("arts")
@RequiredArgsConstructor
public class ArtController {
    private final ArtService artService;

    @PostMapping
    public ArtDto save(@RequestBody ArtDto artDto) {
        return artService.save(artDto);
    }
}

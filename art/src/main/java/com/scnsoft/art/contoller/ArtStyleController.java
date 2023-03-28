package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.service.impl.ArtStyleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("art-styles")
@RequiredArgsConstructor
public class ArtStyleController {

    private final ArtStyleServiceImpl artStyleService;

    @GetMapping
    public List<Option> findAll() {
        return artStyleService.findAll();
    }

}

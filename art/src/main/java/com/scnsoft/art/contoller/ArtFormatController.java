package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.service.impl.ArtFormatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("art-formats")
@RequiredArgsConstructor
@Transactional
public class ArtFormatController {

    private final ArtFormatServiceImpl artFormatService;

    @GetMapping
    public List<Option> findAll() {
        return artFormatService.findAll();
    }

}

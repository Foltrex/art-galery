package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.service.impl.ArtSizeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("art-sizes")
@RequiredArgsConstructor
@Transactional
public class ArtSizeController {

    private final ArtSizeServiceImpl artSizeService;

    @GetMapping
    public List<Option> findAll() {
        return artSizeService.findAll();
    }

}

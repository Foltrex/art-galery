package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.service.impl.ArtTopicServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("art-topics")
@RequiredArgsConstructor
@Transactional
public class ArtTopicController {

    private final ArtTopicServiceImpl artTopicService;

    @GetMapping
    public List<Option> findAll() {
        return artTopicService.findAll();
    }

}

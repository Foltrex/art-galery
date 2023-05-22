package com.scnsoft.art.contoller;

import java.util.UUID;

import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.service.user.MetadataServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RequestMapping("metadatas")
@RestController
@RequiredArgsConstructor
@Transactional
public class MetadataController {
    private final MetadataServiceImpl metadataService;

    @GetMapping
    public Metadata findByKeyAndAccountId(@RequestParam UUID accountId, @RequestParam String key) {
        return metadataService.findByKeyAndAccountId(key, accountId);
    }
}

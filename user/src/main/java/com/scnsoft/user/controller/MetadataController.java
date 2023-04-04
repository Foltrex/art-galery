package com.scnsoft.user.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.service.MetadataService;

import lombok.RequiredArgsConstructor;

@RequestMapping("metadatas")
@RestController
@RequiredArgsConstructor
public class MetadataController {
    private final MetadataService metadataService;

    @GetMapping
    public Metadata findByKeyAndAccountId(@RequestParam UUID accountId, @RequestParam String key) {
        return metadataService.findByKeyAndAccountId(key, accountId);
    }
}

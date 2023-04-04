package com.scnsoft.user.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.repository.MetadataRepository;
import com.scnsoft.user.service.MetadataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
    private final MetadataRepository metadataRepository;

    @Override
    public Metadata findByKeyAndAccountId(String key, UUID accountId) {
        return metadataRepository.findByMetadataIdAccountIdAndMetadataIdKey(accountId, key)
            .orElseThrow();
    }
    
}

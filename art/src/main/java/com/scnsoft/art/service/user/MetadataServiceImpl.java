package com.scnsoft.art.service.user;

import java.util.UUID;

import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.repository.MetadataRepository;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetadataServiceImpl {
    private final MetadataRepository metadataRepository;

    public Metadata findByKeyAndAccountId(String key, UUID accountId) {
        log.info("Your metadata key and account id: {} {}", key, accountId);
        return metadataRepository.findByMetadataIdAccountIdAndMetadataIdKey(accountId, key)
            .orElseThrow();
    }
    
}

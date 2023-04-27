package com.scnsoft.user.service;

import java.util.UUID;

import com.scnsoft.user.entity.Metadata;

public interface MetadataService {
    Metadata findByKeyAndAccountId(String key, UUID accountId);
}

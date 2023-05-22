package com.scnsoft.art.service;

import com.scnsoft.art.dto.AccountDto;

import java.util.UUID;

public interface MetadataValidationService {
    boolean validate(AccountDto accountDto, UUID allowedOrgId);
}

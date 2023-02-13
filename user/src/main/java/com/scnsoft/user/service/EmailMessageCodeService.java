package com.scnsoft.user.service;

import com.scnsoft.user.entity.EmailMessageCode;

import java.util.UUID;

public interface EmailMessageCodeService {

    EmailMessageCode findLastByAccountId(UUID accountId);

    EmailMessageCode save(EmailMessageCode emailMessageCode);

    EmailMessageCode updateById(UUID id, EmailMessageCode emailMessageCode);

    Boolean existWithAccountId(UUID accountId);
}

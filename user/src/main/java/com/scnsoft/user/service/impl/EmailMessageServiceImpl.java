package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.EmailMessageCode;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.EmailMessageCodeRepository;
import com.scnsoft.user.service.EmailMessageCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageServiceImpl implements EmailMessageCodeService {

    private final EmailMessageCodeRepository emailMessageCodeRepository;

    @Override
    public EmailMessageCode findLastByAccountId(UUID accountId) {
        return emailMessageCodeRepository
                .findLastByAccountId(accountId)
                .orElseThrow(() -> new ResourseNotFoundException("Email message not found by accountId: " + accountId));
    }

    @Override
    public EmailMessageCode save(EmailMessageCode emailMessageCode) {
        return emailMessageCodeRepository.save(emailMessageCode);
    }

    @Override
    public EmailMessageCode updateById(UUID id, EmailMessageCode emailMessageCode) {
        emailMessageCode.setId(id);
        return emailMessageCodeRepository.save(emailMessageCode);
    }

    public Boolean existWithAccountId(UUID accountId) {
        return emailMessageCodeRepository
                .findLastByAccountId(accountId)
                .isPresent();
    }

}

package com.scnsoft.art.service.user;

import com.scnsoft.art.entity.EmailMessageCode;
import com.scnsoft.art.repository.EmailMessageCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageServiceImpl {

    private final EmailMessageCodeRepository emailMessageCodeRepository;

    public EmailMessageCode findLastByAccountId(UUID accountId) {
        return emailMessageCodeRepository
                .findLastByAccountId(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email message not found by accountId: " + accountId));
    }

    public EmailMessageCode save(EmailMessageCode emailMessageCode) {
        return emailMessageCodeRepository.save(emailMessageCode);
    }

    public void updateSetCodeIsInvalidById(UUID id, EmailMessageCode emailMessageCode) {
        emailMessageCodeRepository.updateSetCodeIsInvalidById(id);
    }

    public Boolean existWithAccountId(UUID accountId) {
        return emailMessageCodeRepository
                .findLastByAccountId(accountId)
                .isPresent();
    }

}

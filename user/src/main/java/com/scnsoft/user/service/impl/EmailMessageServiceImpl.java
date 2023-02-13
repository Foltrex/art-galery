package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.EmailMessageCode;
import com.scnsoft.user.repository.EmailMessageCodeRepository;
import com.scnsoft.user.service.EmailMessageCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageServiceImpl implements EmailMessageCodeService {

    private final EmailMessageCodeRepository emailMessageCodeRepository;

    @Override
    public EmailMessageCode save(EmailMessageCode emailMessageCode) {
        return emailMessageCodeRepository.save(emailMessageCode);
    }
}

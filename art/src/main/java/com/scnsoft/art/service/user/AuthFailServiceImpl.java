package com.scnsoft.art.service.user;

import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.EmailMessageCode;
import com.scnsoft.art.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthFailServiceImpl {

    private final AccountRepository accountRepository;
    private final EmailMessageServiceImpl emailMessageService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEventOfBadCredentials(Account account) {
        Date prev = account.getLastFail();
        account.setLastFail(new Date());
        if (prev != null && prev.getTime() - account.getLastFail().getTime() < 300_000) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount >= 5) {
                account.setBlockedSince(new Date());
                account.setBlockDuration(300L);
                account.setBlockReason(Account.BlockReason.INVALID_PASSWORD);
            }
        } else {
            account.setFailCount(1);
        }
        accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSetCodeIsInvalidById(UUID id, EmailMessageCode emailMessageCode) {
        emailMessageService.updateSetCodeIsInvalidById(emailMessageCode.getId(), emailMessageCode);
    }
}

package com.scnsoft.user.security.aop;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTypeHandler {

    private final AccountRepository accountRepository;

    public boolean isHasType(String accountType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found!"));

        return account.getAccountType().equals(Account.AccountType.valueOf(accountType));
    }
}

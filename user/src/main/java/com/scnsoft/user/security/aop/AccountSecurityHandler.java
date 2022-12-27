package com.scnsoft.user.security.aop;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountSecurityHandler {

    private final AccountRepository accountRepository;

    public boolean isHasRegisterAccess(String accountType) {
        if (Account.AccountType.valueOf(accountType).equals(Account.AccountType.ARTIST)) {
            return true;
        } else if (Account.AccountType.valueOf(accountType).equals(Account.AccountType.ORGANIZATION)) {
            Account account = getCurrentAccount();
            return account.getAccountType().equals(Account.AccountType.SYSTEM);
        }

        return false;
    }

    public boolean isHasType(Set<String> accountTypes) {
        Account account = getCurrentAccount();
        return accountTypes.contains(account.getAccountType().toString());
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Full authentication is required to access this resource"));
    }
}

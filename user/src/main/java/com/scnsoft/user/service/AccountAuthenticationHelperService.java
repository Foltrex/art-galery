package com.scnsoft.user.service;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.payload.AuthToken;

public interface AccountAuthenticationHelperService {

    void setAccountToAuthentication(String login, String password);

    Account createAccount(String email, String password, Account.AccountType accountType);

    AuthToken createAuthTokenResponse(Account account);

    String encodePassword(String password);

}

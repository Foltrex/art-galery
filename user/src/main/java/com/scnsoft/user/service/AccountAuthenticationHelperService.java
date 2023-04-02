package com.scnsoft.user.service;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.payload.AuthToken;

import java.util.Set;

public interface AccountAuthenticationHelperService {

    void setAccountToAuthentication(String login, String password);

    AuthToken createAuthTokenResponse(Account account);

    String encodePassword(String password);

    Set<Role> getUserRoles();

}

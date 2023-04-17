package com.scnsoft.user.service;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.PasswordRecoveryRequest;

public interface AuthService {

    AuthToken register(AccountDto registrationRequest);

    AuthToken login(LoginRequest loginRequest);

    AccountDto registerUser(AccountDto registeringUser);

    void sendPasswordRecoveryCode(String receiver);

    void passwordRecovery(PasswordRecoveryRequest passwordRecoveryRequest);

    void logout();

}

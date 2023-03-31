package com.scnsoft.user.service;

import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.PasswordRecoveryRequest;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;
import com.scnsoft.user.payload.RegisterRequest;

public interface AuthService {

    AuthToken register(RegisterRequest registerRequest);

    AuthToken login(LoginRequest loginRequest);

    // RepresentativeDto registerRepresentative(RegisterRepresentativeRequest registerRepresentativeRequest);

    void sendPasswordRecoveryCode(String receiver);

    void passwordRecovery(PasswordRecoveryRequest passwordRecoveryRequest);

    void logout();

}

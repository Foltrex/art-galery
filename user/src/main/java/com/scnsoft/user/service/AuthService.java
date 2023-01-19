package com.scnsoft.user.service;

import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.RegisterRequest;

public interface AuthService {

    AuthToken register(RegisterRequest registerRequest);

    AuthToken login(LoginRequest loginRequest);
}

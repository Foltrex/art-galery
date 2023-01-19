package com.scnsoft.user.service;

import com.scnsoft.user.dto.AuthTokenDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;

public interface AuthService {

    AuthTokenDto register(RegisterRequestDto registerRequestDto);

    AuthTokenDto login(LoginRequestDto loginRequestDto);
}

package com.scnsoft.user.service;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.AuthTokenDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;

public interface AccountService {

    AuthTokenDto register(RegisterRequestDto registerRequestDto);

    AuthTokenDto login(LoginRequestDto loginRequestDto);

    AccountDto findByEmail(String email);
}

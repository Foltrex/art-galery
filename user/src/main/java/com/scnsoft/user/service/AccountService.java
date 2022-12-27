package com.scnsoft.user.service;

import com.scnsoft.user.dto.AccountResponseDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;

public interface AccountService {

    AccountResponseDto register(RegisterRequestDto registerRequestDto);

    AccountResponseDto login(LoginRequestDto loginRequestDto);
}

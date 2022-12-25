package com.scnsoft.user.service;

import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.AccountResponseDto;

public interface AccountService {

    AccountResponseDto register(RegisterRequestDto registerRequestDto);

    AccountResponseDto login(LoginRequestDto loginRequestDto);
}

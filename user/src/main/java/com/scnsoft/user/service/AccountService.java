package com.scnsoft.user.service;

import com.scnsoft.user.dto.request.LoginRequestDto;
import com.scnsoft.user.dto.request.RegisterRequestDto;
import com.scnsoft.user.dto.response.AccountResponseDto;

public interface AccountService {

    AccountResponseDto register(RegisterRequestDto registerRequestDto);

    AccountResponseDto login(LoginRequestDto loginRequestDto);
}

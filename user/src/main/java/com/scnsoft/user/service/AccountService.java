package com.scnsoft.user.service;

import com.scnsoft.user.controller.dto.request.LoginRequestDto;
import com.scnsoft.user.controller.dto.request.RegisterRequestDto;
import com.scnsoft.user.controller.dto.response.AccountResponseDto;

public interface AccountService {

    AccountResponseDto register(RegisterRequestDto registerRequestDto);

    AccountResponseDto login(LoginRequestDto loginRequestDto);
}

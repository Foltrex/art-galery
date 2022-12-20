package com.scnsoft.user.service.impl;

import com.scnsoft.user.controller.dto.request.LoginRequestDto;
import com.scnsoft.user.controller.dto.request.RegisterRequestDto;
import com.scnsoft.user.controller.dto.response.AccountResponseDto;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponseDto register(RegisterRequestDto registerRequestDto) {
        return null;
    }

    @Override
    public AccountResponseDto login(LoginRequestDto loginRequestDto) {
        return null;
    }
}

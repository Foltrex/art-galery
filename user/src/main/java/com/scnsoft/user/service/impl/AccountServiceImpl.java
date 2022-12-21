package com.scnsoft.user.service.impl;

import com.scnsoft.user.controller.dto.request.LoginRequestDto;
import com.scnsoft.user.controller.dto.request.RegisterRequestDto;
import com.scnsoft.user.controller.dto.response.AccountResponseDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.RoleRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AccountResponseDto register(RegisterRequestDto registerRequestDto) {
        if (accountRepository.findByLogin(registerRequestDto.getLogin()).isPresent()) {
            throw new LoginAlreadyExistsException("Login is already in use!");
        }

        Account account = Account.builder()
                .login(registerRequestDto.getLogin())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .failCount(0)
                .accountType(Account.AccountType.valueOf(registerRequestDto.getAccountType()))
                .roles(Set.of(roleRepository.findByName(Role.RoleType.ROLE_USER)
                        .orElseThrow(() -> new ResourseNotFoundException("Role not found!"))))
                .build();

        accountRepository.save(account);

        setAccountToAuthentication(registerRequestDto.getLogin(), registerRequestDto.getPassword());
        return createAccountResponse(account);
    }

    @Override
    public AccountResponseDto login(LoginRequestDto loginRequestDto) {
        Account account = accountRepository.findByLogin(loginRequestDto.getLogin())
                .orElseThrow(() -> new ResourseNotFoundException("Account not found!"));

        setAccountToAuthentication(loginRequestDto.getLogin(), loginRequestDto.getPassword());
        return createAccountResponse(account);
    }

    private void setAccountToAuthentication(String login, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
    }

    private AccountResponseDto createAccountResponse(Account account) {

        return AccountResponseDto.builder()
                .id(account.getId())
                .login(account.getLogin())
                .token(jwtUtils.createToken(account.getLogin(), account.getAccountType(), account.getRoles()))
                .build();
    }

}

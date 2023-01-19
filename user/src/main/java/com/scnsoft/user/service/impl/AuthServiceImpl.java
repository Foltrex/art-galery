package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.ArtistDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.RegisterRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.service.AuthService;
import com.scnsoft.user.util.AccountUtil;
import com.scnsoft.user.util.AuthUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AccountUtil accountUtil;
    private final JwtUtils jwtUtils;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;

    @Override
    public AuthToken register(RegisterRequest registerRequest) {
        if (accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }

        Account account = accountUtil.createAccount(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                Account.AccountType.valueOf(registerRequest.getAccountType()));

        accountRepository.save(account);

        String token = jwtUtils.createToken(
                account.getEmail(), account.getId(), account.getAccountType(), account.getRoles());

        try {
            UUID accountId = account.getId();
            switch (account.getAccountType()) {
                case ARTIST -> artistFeignClient.save(ArtistDto.builder().accountId(accountId).build());
                case REPRESENTATIVE -> representativeFeignClient.save(
                        RepresentativeDto.builder().accountId(accountId).build(), "Bearer " + token);
            }
        } catch (FeignException e) {
            accountRepository.delete(account);

            int statusCode = e.status();
            if (statusCode == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.valueOf(statusCode), e.getMessage());
        }

        setAccountToAuthentication(registerRequest.getEmail(), registerRequest.getPassword());
        return createAuthTokenResponse(token);
    }

    @Override
    public AuthToken login(LoginRequest loginRequest) {
        Account account = findByEmail(loginRequest.getEmail());

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = authUtil.calculateSecondsToUnblock(account.getBlockedSince());

            if (secondsToUnblock > 0) {
                throw new AccountBlockedException("Account blocked on " + secondsToUnblock + " seconds");
            }
        }

        try {
            setAccountToAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
            account.setFailCount(0);
            accountRepository.save(account);
        } catch (AuthenticationException e) {
            handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtUtils.createToken(
                account.getEmail(), account.getId(), account.getAccountType(), account.getRoles());

        return createAuthTokenResponse(token);
    }

    private Account findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by email: " + email));
    }

    private void setAccountToAuthentication(String login, String password) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong with authorization");
        }
    }

    private void handleEventOfBadCredentials(Account account) {
        if (authUtil.isBruteForce(account.getLastFail())) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount % 5 == 0) {
                account.setBlockedSince(new Date());
            }
            accountRepository.save(account);
        }
        account.setLastFail(new Date());
    }

    private AuthToken createAuthTokenResponse(String token) {
        return AuthToken.builder()
                .token(token)
                .type("Bearer")
                .build();
    }

}

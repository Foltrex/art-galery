package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.ArtistDto;
import com.scnsoft.user.dto.FacilityDto;
import com.scnsoft.user.dto.OrganizationDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;
import com.scnsoft.user.payload.RegisterRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.service.AuthService;
import com.scnsoft.user.util.AccountAuthenticationUtil;
import com.scnsoft.user.util.TimeUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
    private final AccountAuthenticationUtil accountAuthenticationUtil;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;
    private final AccountService accountService;

    @Override
    public AuthToken register(RegisterRequest registerRequest) {
        if (accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }

        Account account = accountAuthenticationUtil.createAccount(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                Account.AccountType.valueOf(registerRequest.getAccountType()));

        accountRepository.save(account);
        accountAuthenticationUtil.setAccountToAuthentication(registerRequest.getEmail(), registerRequest.getPassword());

        try {
            UUID accountId = account.getId();
            switch (account.getAccountType()) {
                case ARTIST -> artistFeignClient.save(ArtistDto.builder().accountId(accountId).build());
                case REPRESENTATIVE -> representativeFeignClient.save(RepresentativeDto.builder().accountId(accountId).build());
            }
        } catch (FeignException e) {
            logout();
            accountRepository.delete(account);

            int statusCode = e.status();
            if (statusCode == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.valueOf(statusCode), e.getMessage());
        }

        return accountAuthenticationUtil.createAuthTokenResponse(account);
    }

    @Override
    public AuthToken login(LoginRequest loginRequest) {
        Account account = accountService.findByEmail(loginRequest.getEmail());

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = TimeUtil.calculateSecondsToUnblock(account.getBlockedSince());

            if (secondsToUnblock > 0) {
                throw new AccountBlockedException("Account blocked on " + secondsToUnblock + " seconds");
            } else {
                account.setFailCount(0);
            }
        }

        try {
            accountAuthenticationUtil.setAccountToAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (AuthenticationException e) {
            handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }

        return accountAuthenticationUtil.createAuthTokenResponse(account);
    }

    @Override
    public RepresentativeDto registerRepresentative(RegisterRepresentativeRequest registerRepresentativeRequest) {
        if (accountRepository.findByEmail(registerRepresentativeRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }
        Account account = accountAuthenticationUtil.createAccount(
                registerRepresentativeRequest.getEmail(),
                registerRepresentativeRequest.getPassword(),
                Account.AccountType.REPRESENTATIVE);

        accountRepository.save(account);

        RepresentativeDto representativeDto = RepresentativeDto.builder()
                .accountId(account.getId())
                .organization(OrganizationDto.builder().id(registerRepresentativeRequest.getOrganizationId()).build())
                .facility(FacilityDto.builder().id(registerRepresentativeRequest.getFacilityId()).build())
                .build();

        try {
            ResponseEntity<RepresentativeDto> response = representativeFeignClient.save(representativeDto);
            representativeDto = response.getBody();
        } catch (FeignException e) {
            accountRepository.delete(account);

            int statusCode = e.status();
            if (statusCode == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.valueOf(statusCode), e.getMessage());
        }

        return representativeDto;
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private void handleEventOfBadCredentials(Account account) {
        account.setLastFail(new Date());
        if (TimeUtil.isBruteForce(account.getLastFail())) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount % 5 == 0) {
                account.setBlockedSince(new Date());
            }
            accountRepository.save(account);
        }
    }

}

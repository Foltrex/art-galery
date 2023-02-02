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
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.service.AuthService;
import com.scnsoft.user.util.AccountUtil;
import com.scnsoft.user.util.AuthUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AccountUtil accountUtil;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;
    private final AccountService accountService;

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
        return AuthUtil.createAuthTokenResponse(token);
    }

    @Override
    public AuthToken login(LoginRequest loginRequest) {
        Account account = accountService.findByEmail(loginRequest.getEmail());

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = AuthUtil.calculateSecondsToUnblock(account.getBlockedSince());

            if (secondsToUnblock > 0) {
                throw new AccountBlockedException("Account blocked on " + secondsToUnblock + " seconds");
            } else {
                account.setFailCount(0);
            }
        }

        try {
            setAccountToAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (AuthenticationException e) {
            handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtUtils.createToken(
                account.getEmail(), account.getId(), account.getAccountType(), account.getRoles());

        return AuthUtil.createAuthTokenResponse(token);
    }

    @Override
    public RepresentativeDto registerRepresentative(RegisterRepresentativeRequest registerRepresentativeRequest) {
        if (accountRepository.findByEmail(registerRepresentativeRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }
        Account account = accountUtil.createAccount(
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
            HttpServletRequest request = ((ServletRequestAttributes)
                    Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

            String token = request.getHeader("Authorization");

            ResponseEntity<RepresentativeDto> response = representativeFeignClient.save(representativeDto, token);
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

    private void setAccountToAuthentication(String login, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
    }

    private void handleEventOfBadCredentials(Account account) {
        account.setLastFail(new Date());
        if (AuthUtil.isBruteForce(account.getLastFail())) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount % 5 == 0) {
                account.setBlockedSince(new Date());
            }
            accountRepository.save(account);
        }
    }

}

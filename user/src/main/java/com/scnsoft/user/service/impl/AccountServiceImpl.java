package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.ArtistDto;
import com.scnsoft.user.dto.AuthTokenDto;
import com.scnsoft.user.dto.FacilityDto;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.OrganizationDto;
import com.scnsoft.user.dto.RegisterRepresentativeRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.dto.mapper.impl.AccountMapper;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.OrganizationFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.RoleRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.service.AccountService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ArtistFeignClient artistFeignClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final RepresentativeFeignClient representativeFeignClient;
    private final AccountMapper accountMapper;

    @Override
    public AuthTokenDto register(RegisterRequestDto registerRequestDto) {
        if (accountRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }

        Account.AccountType accountType = Account.AccountType.valueOf(registerRequestDto.getAccountType());

        Account account = Account.builder()
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .accountType(accountType)
                .roles(getUserRoles())
                .build();

        accountRepository.save(account);

        try {
            UUID accountId = account.getId();
            switch (accountType) {
                case ARTIST -> {
                    ResponseEntity<ArtistDto> response =
                            artistFeignClient.save(ArtistDto.builder().accountId(accountId).build());

                    log.info(Objects.requireNonNull(response.getBody()).toString());
                }
                case REPRESENTATIVE -> {
                    ResponseEntity<RepresentativeDto> response =
                            representativeFeignClient.save(RepresentativeDto.builder().accountId(accountId).build());

                    log.info(Objects.requireNonNull(response.getBody()).toString());

                }
            }
        } catch (FeignException e) {
            accountRepository.delete(account);

            int statusCode = e.status();
            if (statusCode == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.valueOf(statusCode), e.getMessage());
        }

        setAccountToAuthentication(registerRequestDto.getEmail(), registerRequestDto.getPassword());
        return createAuthTokenResponse(account);
    }

    @Override
    public RepresentativeDto registerRepresentativeToOrganization(RegisterRepresentativeRequestDto registerRepresentativeRequestDto) {
        Account account = Account.builder()
                .email(registerRepresentativeRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRepresentativeRequestDto.getPassword()))
                .accountType(Account.AccountType.REPRESENTATIVE)
                .roles(getUserRoles())
                .build();

        accountRepository.save(account);

        try {
            RepresentativeDto representativeDto = RepresentativeDto.builder()
                    .accountId(account.getId())
                    .organization(OrganizationDto.builder().id(registerRepresentativeRequestDto.getOrganizationId()).build())
                    .facility(FacilityDto.builder().id(registerRepresentativeRequestDto.getFacilityId()).build())
                    .build();

            ResponseEntity<RepresentativeDto> response = representativeFeignClient.save(representativeDto);

            log.info(Objects.requireNonNull(response.getBody()).toString());
        } catch (FeignException e) {
            accountRepository.delete(account);

            int statusCode = e.status();
            if (statusCode == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.valueOf(statusCode), e.getMessage());
        }

        return null;
    }


    @Override
    public AuthTokenDto login(LoginRequestDto loginRequestDto) {
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourseNotFoundException("Account not found!"));

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = calculateSecondsToUnblock(account.getBlockedSince());

            if (secondsToUnblock > 0) {
                throw new AccountBlockedException("Account blocked on " + secondsToUnblock + " seconds");
            }
        }

        try {
            setAccountToAuthentication(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            account.setFailCount(0);
            accountRepository.save(account);
        } catch (AuthenticationException e) {
            handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }

        return createAuthTokenResponse(account);
    }

    @Override
    public AccountDto findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .map(accountMapper::mapToDto)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by email: " + email));
    }

    private Set<Role> getUserRoles() {
        return Set.of(roleRepository
                .findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new ResourseNotFoundException("Role not found!")));
    }

    private void setAccountToAuthentication(String login, String password) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void handleEventOfBadCredentials(Account account) {
        if (isBruteForce(account.getLastFail())) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount % 5 == 0) {
                account.setBlockedSince(new Date());
            }
            accountRepository.save(account);
        }
        account.setLastFail(new Date());
    }

    private AuthTokenDto createAuthTokenResponse(Account account) {
        return AuthTokenDto.builder()
                .id(account.getId())
                .token(jwtUtils.createToken(
                        account.getEmail(), account.getId(), account.getAccountType(), account.getRoles()))
                .build();
    }

    private long calculateSecondsToUnblock(Date blockedSince) {
        long different = new Date().getTime() - blockedSince.getTime();
        long differentInSeconds = TimeUnit.MILLISECONDS.toSeconds(different);

        return 300 - differentInSeconds;
    }

    private boolean isBruteForce(Date lastFail) {
        long different = new Date().getTime() - lastFail.getTime();
        long differentInSeconds = TimeUnit.MILLISECONDS.toSeconds(different);

        return differentInSeconds < 30;
    }

}

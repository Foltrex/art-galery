package com.scnsoft.user.service.impl;

import com.scnsoft.art.entity.Artist;
import com.scnsoft.user.dto.LoginRequestDto;
import com.scnsoft.user.dto.RegisterRequestDto;
import com.scnsoft.user.dto.AccountResponseDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Role;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.RoleRepository;
import com.scnsoft.user.security.JwtUtils;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.util.ArtistFeignClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;
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
    private final ArtistFeignClientUtil artistFeignClientUtil;

    @Override
    public AccountResponseDto register(RegisterRequestDto registerRequestDto) {
        if (accountRepository.findByEmail(registerRequestDto.getLogin()).isPresent()) {
            throw new LoginAlreadyExistsException("Login is already in use!");
        }

        Account account = Account.builder()
                .email(registerRequestDto.getLogin())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .failCount(0)
                .accountType(Account.AccountType.valueOf(registerRequestDto.getAccountType()))
                .roles(Set.of(roleRepository.findByName(Role.RoleType.ROLE_USER)
                        .orElseThrow(() -> new ResourseNotFoundException("Role not found!"))))
                .build();

        accountRepository.save(account);

        Account.AccountType accountType = account.getAccountType();
        switch (accountType) {
            case ARTIST -> {
                Artist artist = Artist.builder()
                        .accountId(account.getId())
                        .build();

                try {
                    artistFeignClientUtil.save(artist);
                } catch (Exception e) {
                    accountRepository.delete(account);
                }
            }
            case ORGANIZATION -> {
                log.info("AAAA");

            }
        }

        setAccountToAuthentication(registerRequestDto.getLogin(), registerRequestDto.getPassword());
        return createAccountResponse(account);
    }

    @Override
    public AccountResponseDto login(LoginRequestDto loginRequestDto) {
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

        return createAccountResponse(account);
    }

    private void setAccountToAuthentication(String login, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
    }

    private void handleEventOfBadCredentials(Account account) {
        Integer failCount = account.getFailCount();
        account.setFailCount(++failCount);
        account.setLastFail(new Date());

        if (failCount % 5 == 0) {
            account.setBlockedSince(new Date());
        }
        accountRepository.save(account);
    }

    private AccountResponseDto createAccountResponse(Account account) {

        return AccountResponseDto.builder()
                .id(account.getId())
                .login(account.getEmail())
                .token(jwtUtils.createToken(account.getEmail(), account.getAccountType(), account.getRoles()))
                .build();
    }

    private long calculateSecondsToUnblock(Date blockedSince) {
        long diff = new Date().getTime() - blockedSince.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        return 300 - seconds;
    }

}

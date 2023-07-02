package com.scnsoft.art.service.user;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AuthToken;
import com.scnsoft.art.dto.EmailMessagePayload;
import com.scnsoft.art.dto.LoginRequest;
import com.scnsoft.art.dto.PasswordRecoveryRequest;
import com.scnsoft.art.dto.mapper.AccountMapper;
import com.scnsoft.art.dto.mapper.MetadataMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.EmailMessageCode;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.constant.TemplateFile;
import com.scnsoft.art.repository.AccountRepository;
import com.scnsoft.art.repository.MetadataRepository;
import com.scnsoft.art.service.EmailSenderService;
import com.scnsoft.art.util.NumberGeneratorUtil;
import com.scnsoft.art.util.PasswordGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl  {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountAuthenticationHelperService accountAuthenticationHelperService;
    private final EmailMessageServiceImpl emailMessageService;
    private final MetadataRepository metadataRepository;
    private final AccountMapper accountMapper;
    private final MetadataMapper metadataMapper;
    private final EmailSenderService emailSenderService;
    private final AuthFailServiceImpl authFailService;

    public AuthToken register(AccountDto registrationRequest) {
        if (accountRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use!");
        }

        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required field");
        }

        Account account = accountRepository.save(Account.builder()
                .id(registrationRequest.getId() == null ? UUID.randomUUID() : registrationRequest.getId())
                .email(registrationRequest.getEmail())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .password(accountAuthenticationHelperService.encodePassword(registrationRequest.getPassword()))
                .accountType(registrationRequest.getAccountType())
                .roles(accountAuthenticationHelperService.getUserRoles())
                .isOneTimePassword(false)
                .build());

        if (registrationRequest.getMetadata() != null) {
            Set<Metadata> metadata = metadataMapper.mapToList(registrationRequest.getMetadata(), account.getId());
            metadataRepository.saveAll(metadata);
        }

        accountAuthenticationHelperService.setAccountToAuthentication(registrationRequest.getEmail(),
                registrationRequest.getPassword());

        return accountAuthenticationHelperService.createAuthTokenResponse(account);
    }

    public AuthToken login(LoginRequest loginRequest) {
        Account account = accountService.findByEmail(loginRequest.getEmail());
        Date blockedSince = account.getBlockedSince();
        Long blockDuration = account.getBlockDuration();

        if (blockedSince != null
                && blockDuration != null) {
            long await = System.currentTimeMillis() - (blockedSince.getTime() + (blockDuration * 1000));
            if(await < -1) {
                await = -1 * await / 1000;
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, await + "");
            }
        }

        try {
            accountAuthenticationHelperService.setAccountToAuthentication(loginRequest.getEmail(),
                    loginRequest.getPassword());
        } catch (AuthenticationException e) {
            authFailService.handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }

        if (account.getIsOneTimePassword()) {
            account.setPassword(null);
            account.setIsOneTimePassword(false);
            accountRepository.save(account);
        }

        return accountAuthenticationHelperService.createAuthTokenResponse(account);
    }

    public AccountDto registerUser(AccountDto registeringUser) {
        Account account = accountMapper.mapToEntity(registeringUser);

        if (accountRepository.findByEmail(registeringUser.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use!");
        }

        String password = PasswordGeneratorUtil.generate(10);

        String encodedPassword = accountAuthenticationHelperService.encodePassword(password);
        account.setMetadata(null);
        account.setPassword(encodedPassword);
        account.setIsOneTimePassword(true);

        Account persistedUser = accountRepository.save(account);

        Set<Metadata> metadata = metadataMapper.mapToList(
                registeringUser.getMetadata(),
                persistedUser.getId());
        metadataRepository.saveAll(metadata);

        Map<String, String> properties = new HashMap<>();
        properties.put("firstname", persistedUser.getFirstName());
        properties.put("lastname", persistedUser.getLastName());
        properties.put("password", password);

        emailSenderService.sendEmailMessage(EmailMessagePayload.builder()
                .receiver(registeringUser.getEmail())
                .subject("Account registration")
                .templateFile(TemplateFile.USER_REGISTRATION)
                .properties(properties)
                .build());

        return accountMapper.mapToDto(persistedUser);
    }

    public void sendPasswordRecoveryCode(String receiver) {
        Account account = accountService.findByEmail(receiver);
        Integer emailCode = NumberGeneratorUtil.generateCode(100000, 999999);
        Map<String, String> properties = new HashMap<>();
        properties.put("email", account.getEmail());
        properties.put("code", emailCode.toString());

        emailSenderService.sendEmailMessage(EmailMessagePayload.builder()
                .receiver(receiver)
                .subject("Password recovery")
                .templateFile(TemplateFile.PASSWORD_RECOVERY)
                .properties(properties)
                .build());


        if (emailMessageService.existWithAccountId(account.getId())) {
            EmailMessageCode lastEmailMessageCode = emailMessageService.findLastByAccountId(account.getId());
            emailMessageService.updateSetCodeIsInvalidById(lastEmailMessageCode.getId(), lastEmailMessageCode);
        }

        emailMessageService.save(EmailMessageCode.builder()
                .account(account)
                .code(emailCode)
                .build());
    }

    public void passwordRecovery(PasswordRecoveryRequest passwordRecoveryRequest) {
        Account account = accountService.findByEmail(passwordRecoveryRequest.getEmail());
        EmailMessageCode emailMessageCode = emailMessageService.findLastByAccountId(account.getId());

        if (!emailMessageCode.getIsValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is invalid, try to send code again!");
        }

        if (!emailMessageCode.getCode().equals(passwordRecoveryRequest.getCode())) {
            emailMessageCode.setCountAttempts(emailMessageCode.getCountAttempts() + 1);
            if (emailMessageCode.getCountAttempts().equals(5)) {
                authFailService.updateSetCodeIsInvalidById(emailMessageCode.getId(), emailMessageCode);
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is not correct, try again!");
        }

        account.setPassword(
                accountAuthenticationHelperService.encodePassword(passwordRecoveryRequest.getPassword()));
        accountRepository.save(account);

        emailMessageService.updateSetCodeIsInvalidById(emailMessageCode.getId(), emailMessageCode);
    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}

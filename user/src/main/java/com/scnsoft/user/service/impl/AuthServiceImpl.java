package com.scnsoft.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Strings;
import com.scnsoft.user.dto.AccountDto;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.scnsoft.user.dto.mapper.AccountMapper;
import com.scnsoft.user.dto.mapper.MetadataMapper;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.EmailMessageCode;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import com.scnsoft.user.entity.OrganizationRole;
import com.scnsoft.user.entity.Account.AccountType;
import com.scnsoft.user.entity.constant.MetadataEnum;
import com.scnsoft.user.entity.constant.TemplateFile;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.FeignResponseException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.WrongAccessPermissionException;
import com.scnsoft.user.feignclient.NotificationFeignClient;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.EmailMessagePayload;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.PasswordRecoveryRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.repository.MetadataRepository;
import com.scnsoft.user.security.aop.AccountSecurityHandler;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.service.AuthService;
import com.scnsoft.user.service.EmailMessageCodeService;
import com.scnsoft.user.util.AuthHelperUtil;
import com.scnsoft.user.util.NumberGeneratorUtil;
import com.scnsoft.user.util.PasswordGeneratorUtil;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountAuthenticationHelperServiceImpl accountAuthenticationHelperServiceImpl;
    private final AccountSecurityHandler accountSecurityHandler;
    private final NotificationFeignClient notificationFeignClient;
    private final EmailMessageCodeService emailMessageService;
    private final MetadataRepository metadataRepository;
    private final AccountMapper accountMapper;
    private final MetadataMapper metadataMapper;

    @Override
    public AuthToken register(AccountDto registrationRequest) {
        if (accountRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("Email is already in use!");
        }

        if (Strings.isNullOrEmpty(registrationRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required field");
        }

        Account account = accountRepository.save(Account.builder()
                .id(registrationRequest.getId() == null ? UUID.randomUUID() : registrationRequest.getId())
                .email(registrationRequest.getEmail())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .password(accountAuthenticationHelperServiceImpl.encodePassword(registrationRequest.getPassword()))
                .accountType(registrationRequest.getAccountType())
                .roles(accountAuthenticationHelperServiceImpl.getUserRoles())
                .isOneTimePassword(false)
                .build());

        Set<Metadata> metadata = metadataMapper.mapToList(registrationRequest.getMetadata(), account.getId());
        metadataRepository.saveAll(metadata);

        accountAuthenticationHelperServiceImpl.setAccountToAuthentication(registrationRequest.getEmail(), registrationRequest.getPassword());

        return accountAuthenticationHelperServiceImpl.createAuthTokenResponse(account);
    }

    @Override
    public AuthToken login(LoginRequest loginRequest) {
        Account account = accountService.findByEmail(loginRequest.getEmail());

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = AuthHelperUtil.calculateSecondsToUnblock(account.getBlockedSince());

            if (secondsToUnblock > 0) {
                throw new AccountBlockedException("Account blocked on " + secondsToUnblock + " seconds");
            }
            account.setFailCount(0);
        }

        try {
            accountAuthenticationHelperServiceImpl.setAccountToAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (AuthenticationException e) {
            handleEventOfBadCredentials(account);
            throw new BadCredentialsException("Invalid credentials");
        }

        if (account.getIsOneTimePassword()) {
            account.setPassword(null);
            account.setIsOneTimePassword(false);
            accountRepository.save(account);
        }

        return accountAuthenticationHelperServiceImpl.createAuthTokenResponse(account);
    }

    @Override
    public AccountDto registerUser(AccountDto registeringUser) {
        Account account = accountMapper.mapToEntity(registeringUser);

        if (accountRepository.findByEmail(registeringUser.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("Email is already in use!");
        }

        Account currentLoggedUser = accountSecurityHandler.getCurrentAccount();
        if (!isLoggedUserAbleToCreateUser(account)) {
            throw new WrongAccessPermissionException(currentLoggedUser + " can't create user " + account + " because of permissions");
        }

        String password = PasswordGeneratorUtil.generate(10);

        String encodedPassword = accountAuthenticationHelperServiceImpl.encodePassword(password);
        account.setMetadata(null);
        account.setPassword(encodedPassword);
        account.setIsOneTimePassword(true);

        Account persistedUser = accountRepository.save(account);

        Set<Metadata> metadata = metadataMapper.mapToList(
            registeringUser.getMetadata(), 
            persistedUser.getId()
        );
        metadataRepository.saveAll(metadata);


        Map<String, String> properties = new HashMap<>();
        properties.put("firstname", persistedUser.getFirstName());
        properties.put("lastname", persistedUser.getLastName());
        properties.put("password", password);
        
        notificationFeignClient.sendMessage(EmailMessagePayload.builder()
            .receiver(registeringUser.getEmail())
            .subject("Account registration")
            .templateFile(TemplateFile.USER_REGISTRATION)
            .properties(properties)
            .build());

        return accountMapper.mapToDto(persistedUser);
    }

    @Override
    public void sendPasswordRecoveryCode(String receiver) {
        Account account = accountService.findByEmail(receiver);
        Integer emailCode = NumberGeneratorUtil.generateCode(100000, 999999);
        Map<String, String> properties = new HashMap<>();
        properties.put("email", account.getEmail());
        properties.put("code", emailCode.toString());

        try {
            notificationFeignClient.sendMessage(EmailMessagePayload.builder()
                    .receiver(receiver)
                    .subject("Password recovery")
                    .templateFile(TemplateFile.PASSWORD_RECOVERY)
                    .properties(properties)
                    .build());
        } catch (FeignException e) {
            throw new FeignResponseException(e);
        }

        if (emailMessageService.existWithAccountId(account.getId())) {
            EmailMessageCode lastEmailMessageCode = emailMessageService.findLastByAccountId(account.getId());
            emailMessageService.updateSetCodeIsInvalidById(lastEmailMessageCode.getId(), lastEmailMessageCode);
        }

        emailMessageService.save(EmailMessageCode.builder()
                .account(account)
                .code(emailCode)
                .build());
    }

    @Override
    public void passwordRecovery(PasswordRecoveryRequest passwordRecoveryRequest) {
        Account account = accountService.findByEmail(passwordRecoveryRequest.getEmail());
        EmailMessageCode emailMessageCode = emailMessageService.findLastByAccountId(account.getId());

        if (!emailMessageCode.getIsValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is invalid, try to send code again!");
        }

        if (!emailMessageCode.getCode().equals(passwordRecoveryRequest.getCode())) {
            emailMessageCode.setCountAttempts(emailMessageCode.getCountAttempts() + 1);
            if (emailMessageCode.getCountAttempts().equals(5)) {
                emailMessageService.updateSetCodeIsInvalidById(emailMessageCode.getId(), emailMessageCode);
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is not correct, try again!");
        }

        account.setPassword(accountAuthenticationHelperServiceImpl.encodePassword(passwordRecoveryRequest.getPassword()));
        accountRepository.save(account);

        emailMessageService.updateSetCodeIsInvalidById(emailMessageCode.getId(), emailMessageCode);
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private void handleEventOfBadCredentials(Account account) {
        account.setLastFail(new Date());
        if (AuthHelperUtil.isBruteForce(account.getLastFail())) {
            Integer failCount = account.getFailCount();
            account.setFailCount(++failCount);

            if (failCount % 5 == 0) {
                account.setBlockedSince(new Date());
            }
            accountRepository.save(account);
        }
    }

    private boolean isLoggedUserAbleToCreateUser(Account creatingUser) {
        Account currentLoggedUser = accountSecurityHandler.getCurrentAccount();

        boolean isUsersAreBothRepresentatives = 
            currentLoggedUser.getAccountType() == AccountType.REPRESENTATIVE
            && creatingUser.getAccountType() == AccountType.REPRESENTATIVE;


        UUID loggedUserOrganizationId = extractOrganizationIdFromAccount(currentLoggedUser);
        UUID registeringUserOrganizationId = extractOrganizationIdFromAccount(creatingUser);
        boolean isUserWorksInTheSameOrganizaiton = loggedUserOrganizationId.equals(registeringUserOrganizationId);

        OrganizationRole loggedUserRole = extracOrganizationRoleFrom(currentLoggedUser);
        OrganizationRole registeringUserOrganizationRole = extracOrganizationRoleFrom(creatingUser);

        boolean isBossRole = loggedUserRole.equals(OrganizationRole.CREATOR)
            || loggedUserRole.equals(OrganizationRole.MODERATOR) && registeringUserOrganizationRole.equals(OrganizationRole.MEMBER);

        
        return currentLoggedUser.getAccountType().equals(AccountType.SYSTEM)
            || (isUsersAreBothRepresentatives && isUserWorksInTheSameOrganizaiton && isBossRole);
    }

    private UUID extractOrganizationIdFromAccount(Account account) {
        String uuidString = extractMetadataValueByKeyFromAccount(MetadataEnum.ORGANIZATION_ID, account);
        return UUID.fromString(uuidString);
    }

    private OrganizationRole extracOrganizationRoleFrom(Account account) {
        String organizationRoleValue = extractMetadataValueByKeyFromAccount(MetadataEnum.ORGANIZATION_ROLE, account);
        return OrganizationRole.valueOf(organizationRoleValue);
    } 

    private String extractMetadataValueByKeyFromAccount(MetadataEnum key, Account account) {
        return account.getMetadata()
        .stream()
        .filter(m -> {
            MetadataId metadataId = m.getMetadataId();
            return Objects.equals(key.getValue(), metadataId.getKey());
        })
        .findFirst()
        .map(Metadata::getValue)
        .orElseThrow(() -> new WrongAccessPermissionException("User must have " + key.getValue() +": " + account));
    } 
}

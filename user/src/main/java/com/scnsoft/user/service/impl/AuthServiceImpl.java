package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.ArtistDto;
import com.scnsoft.user.dto.FacilityDto;
import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.dto.OrganizationDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.EmailMessageCode;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import com.scnsoft.user.entity.constant.TemplateFile;
import com.scnsoft.user.exception.AccountBlockedException;
import com.scnsoft.user.exception.FeignResponseException;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.NotificationFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.payload.AuthToken;
import com.scnsoft.user.payload.EmailMessagePayload;
import com.scnsoft.user.payload.LoginRequest;
import com.scnsoft.user.payload.PasswordRecoveryRequest;
import com.scnsoft.user.payload.RegisterRepresentativeRequest;
import com.scnsoft.user.payload.RegisterRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.service.AuthService;
import com.scnsoft.user.service.EmailMessageCodeService;
import com.scnsoft.user.util.NumberGeneratorUtil;
import com.scnsoft.user.util.PasswordGeneratorUtil;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountAuthenticationHelperServiceImpl accountAuthenticationHelperServiceImpl;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;
    private final NotificationFeignClient notificationFeignClient;
    private final EmailMessageCodeService emailMessageService;

    @Override
    public AuthToken register(RegisterRequest registerRequest) {
        if (accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("Email is already in use!");
        }

        UUID uuid = UUID.randomUUID();

        Account account = accountAuthenticationHelperServiceImpl.createAccount(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                Account.AccountType.valueOf(registerRequest.getAccountType()));
        account.setIsOneTimePassword(false);
        account.setId(uuid);
//        account = accountRepository.save(account);

        List<MetadataDto> metadataDtosList = registerRequest.getMetadata();
        List<Metadata> metadataList = new ArrayList<>();
        for (MetadataDto metadataDto : metadataDtosList) {
            MetadataId metadataId = new MetadataId();
            metadataId.setAccountId(uuid);
            metadataId.setKey(metadataDto.getKey());
            Metadata metadata = Metadata.builder()
                    .metadataId(metadataId)
                    .value(metadataDto.getValue())
                    .build();
            metadataList.add(metadata);
        }
//        metadataDtosList.forEach(metadataDto -> {
//
//
//        });
        account.setMetadata(metadataList);
        log.info(String.valueOf(account));
        accountRepository.save(account);


        accountAuthenticationHelperServiceImpl.setAccountToAuthentication(registerRequest.getEmail(), registerRequest.getPassword());

        try {
            UUID accountId = account.getId();
            switch (account.getAccountType()) {
                case ARTIST -> artistFeignClient.save(ArtistDto.builder().accountId(accountId).build());
                case REPRESENTATIVE -> representativeFeignClient.save(RepresentativeDto.builder().accountId(accountId).build());
            }
        } catch (FeignException e) {
            logout();
            accountRepository.delete(account);
            throw new FeignResponseException(e);
        }

        return accountAuthenticationHelperServiceImpl.createAuthTokenResponse(account);
    }

    @Override
    public AuthToken login(LoginRequest loginRequest) {
        Account account = accountService.findByEmail(loginRequest.getEmail());

        Integer failCount = account.getFailCount();
        if (failCount != 0 && failCount % 5 == 0) {
            long secondsToUnblock = TimeUtil.calculateSecondsToUnblock(account.getBlockedSince());

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
    public RepresentativeDto registerRepresentative(RegisterRepresentativeRequest registerRepresentativeRequest) {
        if (accountRepository.findByEmail(registerRepresentativeRequest.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("Email is already in use!");
        }

        String password = PasswordGeneratorUtil.generate(10);

        Account account = accountAuthenticationHelperServiceImpl.createAccount(
                registerRepresentativeRequest.getEmail(), password, Account.AccountType.REPRESENTATIVE);

        account.setIsOneTimePassword(true);

        accountRepository.save(account);

        RepresentativeDto representativeDto = RepresentativeDto.builder()
                .accountId(account.getId())
                .organization(OrganizationDto.builder().id(registerRepresentativeRequest.getOrganizationId()).build())
                .facility(FacilityDto.builder().id(registerRepresentativeRequest.getFacilityId()).build())
                .build();

        Map<String, String> properties = new HashMap<>();
        properties.put("firstname", registerRepresentativeRequest.getFirstname());
        properties.put("lastname", registerRepresentativeRequest.getLastname());
        properties.put("password", password);

        try {
            ResponseEntity<RepresentativeDto> response = representativeFeignClient.save(representativeDto);
            representativeDto = response.getBody();

            notificationFeignClient.sendMessage(EmailMessagePayload.builder()
                    .receiver(registerRepresentativeRequest.getEmail())
                    .subject("Account registration")
                    .templateFile(TemplateFile.REPRESENTATIVE_REGISTRATION)
                    .properties(properties)
                    .build()
            );
        } catch (FeignException e) {
            accountRepository.delete(account);
            throw new FeignResponseException(e);
        }

        return representativeDto;
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

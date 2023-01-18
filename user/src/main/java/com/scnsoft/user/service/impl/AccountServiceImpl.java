package com.scnsoft.user.service.impl;

import com.scnsoft.user.dto.FacilityDto;
import com.scnsoft.user.dto.OrganizationDto;
import com.scnsoft.user.dto.RegisterRepresentativeRequestDto;
import com.scnsoft.user.dto.RepresentativeDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.LoginAlreadyExistsException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import com.scnsoft.user.util.AccountUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountUtil accountUtil;
    private final RepresentativeFeignClient representativeFeignClient;


    @Override
    public RepresentativeDto registerRepresentative(RegisterRepresentativeRequestDto registerRepresentativeRequestDto) {
        if (accountRepository.findByEmail(registerRepresentativeRequestDto.getEmail()).isPresent()) {
            throw new LoginAlreadyExistsException("email is already in use!");
        }
        Account account = accountUtil.createAccount(
                registerRepresentativeRequestDto.getEmail(),
                registerRepresentativeRequestDto.getPassword(),
                Account.AccountType.REPRESENTATIVE);

        accountRepository.save(account);

        RepresentativeDto representativeDto = RepresentativeDto.builder()
                .accountId(account.getId())
                .organization(OrganizationDto.builder().id(registerRepresentativeRequestDto.getOrganizationId()).build())
                .facility(FacilityDto.builder().id(registerRepresentativeRequestDto.getFacilityId()).build())
                .build();

        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

            String authorization = request.getHeader("Authorization");
            log.info(authorization);

            ResponseEntity<RepresentativeDto> response = representativeFeignClient.save(representativeDto, authorization);
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
    public Account findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by email: " + email));
    }

}

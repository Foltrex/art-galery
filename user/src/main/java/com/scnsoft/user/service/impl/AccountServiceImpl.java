package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.FeignResponseException;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.payload.UpdatePasswordRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account findById(UUID id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by id: " + id));
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourseNotFoundException("Account not found by email: " + email));
    }

    @Override
    public void updatePasswordById(UUID id, UpdatePasswordRequest updatePasswordRequest) {
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();
        Account account = findById(id);

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is not correct!");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Account account = findById(id);
        accountRepository.delete(account);
        try {
            switch (account.getAccountType()) {
                case REPRESENTATIVE -> representativeFeignClient.deleteByAccountId(account.getId());
                case ARTIST -> artistFeignClient.deleteByAccountId(account.getId());
            }
        } catch (FeignException e) {
            throw new FeignResponseException(e);
        }
    }

}

package com.scnsoft.user.service.impl;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.exception.ResourseNotFoundException;
import com.scnsoft.user.feignclient.ArtistFeignClient;
import com.scnsoft.user.feignclient.RepresentativeFeignClient;
import com.scnsoft.user.payload.DeleteAccountRequest;
import com.scnsoft.user.repository.AccountRepository;
import com.scnsoft.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RepresentativeFeignClient representativeFeignClient;
    private final ArtistFeignClient artistFeignClient;

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
    @Transactional
    public void deleteAccountById(DeleteAccountRequest deleteAccountRequest) {
        Account account = findById(deleteAccountRequest.getAccountId());

        switch (account.getAccountType()) {
            case REPRESENTATIVE -> {

            }
            case ARTIST -> {

            }
        }
    }

}

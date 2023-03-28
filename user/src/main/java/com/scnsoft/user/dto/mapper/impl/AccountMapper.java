package com.scnsoft.user.dto.mapper.impl;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.dto.mapper.Mapper;
import com.scnsoft.user.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements Mapper<Account, AccountDto> {

    @Override
    public AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .blockedSince(account.getBlockedSince())
                .failCount(account.getFailCount())
                .lastFail(account.getLastFail())
                .accountType(account.getAccountType().toString())
                .metadata(account.getMetadata())
                .build();
    }

    @Override
    public Account mapToEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .email(accountDto.getEmail())
                .blockedSince(accountDto.getBlockedSince())
                .failCount(accountDto.getFailCount())
                .lastFail(accountDto.getLastFail())
                .accountType(Account.AccountType.valueOf(accountDto.getAccountType()))
                .build();
    }

}

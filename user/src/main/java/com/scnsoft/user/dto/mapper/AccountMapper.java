package com.scnsoft.user.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    public AccountDto mapToDto(Account account);

    public Account mapToEntity(AccountDto accountDto);
}

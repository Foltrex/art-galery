package com.scnsoft.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Account.AccountType;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface AccountMapper {

    AccountDto mapToDto(Account account);

    @Mapping(target = "password", ignore = true)
    Account mapToEntity(AccountDto accountDto);
}

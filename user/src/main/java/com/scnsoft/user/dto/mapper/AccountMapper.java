package com.scnsoft.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scnsoft.user.dto.AccountDto;
import com.scnsoft.user.entity.Account;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface AccountMapper {

    @Mapping(target = "password", ignore = true)
    AccountDto mapToDto(Account account);

    @Mapping(target = "password", ignore = true)
    Account mapToEntity(AccountDto accountDto);
}

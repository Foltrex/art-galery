package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface AccountMapper {

    @Mapping(target = "password", ignore = true)
    AccountDto mapToDto(Account account);

    @Mapping(target = "password", ignore = true)
    Account mapToEntity(AccountDto accountDto);
}

package com.scnsoft.art.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.art.dto.CurrencyDto;
import com.scnsoft.art.entity.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto mapToDto(Currency currency);

    Currency mapToEntity(CurrencyDto currencyDto);
}

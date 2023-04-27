package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.CurrencyDto;
import com.scnsoft.art.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto mapToDto(Currency currency);

    Currency mapToEntity(CurrencyDto currencyDto);
}

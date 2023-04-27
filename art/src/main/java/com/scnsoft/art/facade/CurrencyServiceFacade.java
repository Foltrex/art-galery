package com.scnsoft.art.facade;

import com.scnsoft.art.dto.CurrencyDto;
import com.scnsoft.art.dto.mapper.CurrencyMapper;
import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CurrencyServiceFacade {
    private final CurrencyMapper currencyMapper;
    private final CurrencyService currencyService;

    public CurrencyDto save(CurrencyDto currencyDto) {
        Currency currency = currencyMapper.mapToEntity(currencyDto);
        return currencyMapper.mapToDto(currencyService.save(currency));
    }

    public List<CurrencyDto> findAll() {
        return currencyService.findAll()
                .stream()
                .map(currencyMapper::mapToDto)
                .toList();
    }
}

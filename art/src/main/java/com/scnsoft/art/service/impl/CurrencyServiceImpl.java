package com.scnsoft.art.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.repository.CurrencyRepository;
import com.scnsoft.art.service.CurrencyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }
}

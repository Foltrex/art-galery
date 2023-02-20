package com.scnsoft.art.service;

import java.util.List;

import com.scnsoft.art.entity.Currency;

public interface CurrencyService {
    List<Currency> findAll();

    Currency save(Currency currency);
}

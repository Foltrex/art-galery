package com.scnsoft.art.service;

import com.scnsoft.art.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> findAll();

    Currency save(Currency currency);
}

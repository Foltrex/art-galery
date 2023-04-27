package com.scnsoft.art.app;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppInitialization {
    private static final String DOLLAR_CURRENCY_VALUE = "USD";
    private static final String DOLLAR_CURRENCY_LABEL = "$";

    private static final String GEORGIAN_CURRENCY_VALUE = "GEL";
    private static final String GEORGIAN_CURRENCY_LABEL = "ლ";
    
    private final CurrencyRepository currencyRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        initCurrencies();
    }

    @Transactional
    public void initCurrencies() {
        if (currencyRepository.findByValue(DOLLAR_CURRENCY_VALUE).isEmpty()) {
            Currency dollarCurrency = Currency.builder()
                    .value(DOLLAR_CURRENCY_VALUE)
                    .label(DOLLAR_CURRENCY_LABEL)
                    .build();

            currencyRepository.save(dollarCurrency);
        }

        if (currencyRepository.findByValue(GEORGIAN_CURRENCY_VALUE).isEmpty()) {
            Currency georgianCurrency = Currency.builder()
                    .value(GEORGIAN_CURRENCY_VALUE)
                    .label(GEORGIAN_CURRENCY_LABEL)
                    .build();

            currencyRepository.save(georgianCurrency);
        }
    }

}

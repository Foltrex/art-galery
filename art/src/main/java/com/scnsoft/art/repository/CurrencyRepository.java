package com.scnsoft.art.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
    Optional<Currency> findByValue(String value);
}

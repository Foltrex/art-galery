package com.scnsoft.art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
}

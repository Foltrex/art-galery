package com.scnsoft.art.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.City;

import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    Optional<City> findByNameAndCountry(String name, String country);
}

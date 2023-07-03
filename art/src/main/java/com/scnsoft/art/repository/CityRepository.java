package com.scnsoft.art.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.City;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    Optional<City> findByNameAndCountry(String name, String country);

    @Query("select c from City c where c.successor is null")
    List<City> findNotObsolete();
}

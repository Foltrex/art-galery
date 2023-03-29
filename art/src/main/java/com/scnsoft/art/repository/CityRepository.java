package com.scnsoft.art.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.user.entity.City;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
}

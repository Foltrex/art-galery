package com.scnsoft.art.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.scnsoft.art.entity.City;

public interface CityRepository extends JpaRepository<City, UUID> {
}

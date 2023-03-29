package com.scnsoft.art.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scnsoft.user.entity.City;

import java.util.UUID;

public interface CityService {

    Page<City> findAll(Pageable pageable);

    City findById(UUID id);

    City save(City city);

    City updateById(UUID id, City city);

    void deleteById(UUID id);
}

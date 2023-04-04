package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.entity.City;

public interface CityService {

    Page<City> findAll(Pageable pageable);

    List<City> findAll();

    City findById(UUID id);

    City save(City city);

    City updateById(UUID id, City city);

    void deleteById(UUID id);
}

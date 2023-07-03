package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.CityMergeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.entity.City;

public interface CityService {

    Page<City> findAll(Pageable pageable);

    List<City> findAll();

    City findById(UUID id);

    City save(City city);
    City save(City city, boolean force);

    City updateById(UUID id, City city);

    void deleteById(UUID id);

    City merge(CityMergeDto cityDto);
}

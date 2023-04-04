package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.City;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.CityRepository;
import com.scnsoft.art.service.CityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(UUID id) {
        return cityRepository
                .findById(id)
                .orElseThrow(() -> new ArtResourceNotFoundException("City not found by id"));
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City updateById(UUID id, City city) {
        city.setId(id);
        return cityRepository.save(city);
    }

    @Override
    public void deleteById(UUID id) {
        cityRepository.deleteById(id);
    }
}

package com.scnsoft.art.service;

import com.scnsoft.art.dto.mapper.impl.CityMapper;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record CityServiceImpl(
        CityRepository cityRepository,
        CityMapper cityMapper
) implements CityService {


    @Override
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public City findById(UUID id) {
        return cityRepository
                .findById(id)
                .orElseThrow(ArtResourceNotFoundException::new);
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

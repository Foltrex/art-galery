package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.City;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.CityRepository;

@Service
public record CityService(CityRepository cityRepository) {
    
    
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findById(UUID id) {
        return cityRepository
            .findById(id)
            .orElseThrow(ArtResourceNotFoundException::new);
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public City update(UUID id, City city) {
        city.setId(id);
        return cityRepository.save(city);
    }

    public void deleteById(UUID id) {
        cityRepository.deleteById(id);
    }
}

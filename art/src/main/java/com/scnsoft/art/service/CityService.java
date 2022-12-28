package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.impl.CityMapper;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.CityRepository;

@Service
public record CityService(
    CityRepository cityRepository, 
    CityMapper cityMapper
) {
    
    
    public List<CityDto> findAll() {
        return cityRepository.findAll()
            .stream()
            .map(cityMapper::mapToDto)
            .toList();
    }

    public CityDto findById(UUID id) {
        return cityRepository
            .findById(id)
            .map(cityMapper::mapToDto)
            .orElseThrow(ArtResourceNotFoundException::new);
    }

    public CityDto save(CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        City persistedCity = cityRepository.save(city);
        return cityMapper.mapToDto(persistedCity);
    }

    public CityDto update(UUID id, CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        city.setId(id);
        City updatedCity = cityRepository.save(city);
        return cityMapper.mapToDto(updatedCity);
    }

    public void deleteById(UUID id) {
        cityRepository.deleteById(id);
    }
}

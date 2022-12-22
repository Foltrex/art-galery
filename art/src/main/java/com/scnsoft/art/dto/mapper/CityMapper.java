package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.entity.City;

@Component
public class CityMapper {
    public CityDto mapToDto(City city) {
        return CityDto.builder()
            .id(city.getId())
            .name(city.getName())
            .latitude(city.getLatitude())
            .longitude(city.getLongitude())
            .build();
    }

    public City mapToEntity(CityDto cityDto) {
        return City.builder()
            .id(cityDto.id())
            .name(cityDto.name())
            .latitude(cityDto.latitude())
            .longitude(cityDto.longitude())
            .build();
    }
}

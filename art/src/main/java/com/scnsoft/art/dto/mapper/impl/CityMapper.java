package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper implements Mapper<City, CityDto> {
    @Override
    public CityDto mapToDto(City city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .build();
    }

    @Override
    public City mapToEntity(CityDto cityDto) {
        return City.builder()
                .id(cityDto.getId())
                .name(cityDto.getName())
                .latitude(cityDto.getLatitude())
                .longitude(cityDto.getLongitude())
                .build();
    }
}

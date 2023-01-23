package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

    public Page<CityDto> mapPageToDto(final Page<City> cityPage) {
        return new PageImpl<>(cityPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), cityPage.getPageable(), cityPage.getTotalElements());

    }
}

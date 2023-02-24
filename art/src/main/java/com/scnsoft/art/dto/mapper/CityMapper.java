package com.scnsoft.art.dto.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.entity.City;

@Mapper(componentModel = "spring")
public abstract class CityMapper {

    public abstract CityDto mapToDto(City city);

    public abstract City mapToEntity(CityDto cityDto);

    public Page<CityDto> mapPageToDto(final Page<City> cityPage) {
        return new PageImpl<>(cityPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), cityPage.getPageable(), cityPage.getTotalElements());
    }
}

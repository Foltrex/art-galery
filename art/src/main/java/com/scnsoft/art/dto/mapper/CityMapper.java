package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.repository.CityRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CityRepository.class})
public abstract class CityMapper {

    @Autowired
    CityRepository cityRepository;

    public abstract CityDto mapToDto(City city);

    public City mapToEntity(CityDto cityDto) {
        return cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry())
                .orElseGet(() -> {
                    City city = new City();
                    city.setId(cityDto.getId());
                    city.setName(cityDto.getName());
                    city.setCountry(cityDto.getCountry());
                    city.setLongitude(cityDto.getLongitude());
                    city.setLatitude(cityDto.getLatitude());
                    return city;
                });
    }

    public Page<CityDto> mapPageToDto(final Page<City> cityPage) {
        return new PageImpl<>(cityPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), cityPage.getPageable(), cityPage.getTotalElements());
    }
}

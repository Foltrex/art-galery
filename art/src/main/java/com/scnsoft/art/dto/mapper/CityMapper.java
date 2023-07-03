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
        City city = null;
        if(cityDto.getId() != null) {
            city = cityRepository.findById(cityDto.getId()).orElse(null);
        }
        if(city == null) {
            city = cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry()).orElse(null);
        }
        if(city == null) {
            city = new City();
        }
        city.setName(cityDto.getName());
        city.setCountry(cityDto.getCountry());
        city.setLongitude(cityDto.getLongitude());
        city.setLatitude(cityDto.getLatitude());
        city.setSuccessor(cityDto.getSuccessor());
        return city;
    }

    public Page<CityDto> mapPageToDto(final Page<City> cityPage) {
        return new PageImpl<>(cityPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), cityPage.getPageable(), cityPage.getTotalElements());
    }
}

package com.scnsoft.art.facade;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.CityMapper;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.service.impl.CityServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CityServiceFacade {

    private final CityServiceImpl cityService;
    private final CityMapper cityMapper;


    public Page<CityDto> findAll(Pageable pageable) {
        return cityMapper.mapPageToDto(cityService.findAll(pageable));
    }

    public List<CityDto> findAll() {
        return cityService.findAll()
            .stream()
            .map(cityMapper::mapToDto)
            .toList();
    }

    public CityDto findById(UUID id) {
        return cityMapper.mapToDto(cityService.findById(id));
    }

    public CityDto save(CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        return cityMapper.mapToDto(cityService.save(city));
    }

    public CityDto updateById(UUID id, CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        return cityMapper.mapToDto(cityService.updateById(id, city));
    }

    public Void deleteById(@PathVariable UUID id) {
        cityService.deleteById(id);
        return null;
    }

}

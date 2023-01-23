package com.scnsoft.art.facade;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.impl.CityMapper;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.service.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CityServiceFacade {

    private final CityServiceImpl cityService;
    private final CityMapper cityMapper;


    public Page<CityDto> findAll(Pageable pageable) {
        return cityMapper.mapPageToDto(cityService.findAll(pageable));
    }

    public CityDto findById(UUID id) {
        return cityMapper.mapToDto(cityService.findById(id));
    }

    public CityDto save(CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        return cityMapper.mapToDto(cityService.save(city));
    }

    public CityDto update(UUID id, CityDto cityDto) {
        City city = cityMapper.mapToEntity(cityDto);
        return cityMapper.mapToDto(cityService.updateById(id, city));
    }

    public Void deleteById(@PathVariable UUID id) {
        cityService.deleteById(id);
        return null;
    }

}

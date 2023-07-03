package com.scnsoft.art.facade;

import java.util.List;
import java.util.UUID;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.CityMergeDto;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.AddressService;
import com.scnsoft.art.service.CityService;
import com.scnsoft.art.service.user.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.scnsoft.art.dto.CityDto;
import com.scnsoft.art.dto.mapper.CityMapper;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.service.impl.CityServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CityServiceFacade {

    private final CityService cityService;
    private final CityMapper cityMapper;
    private final AccountService accountService;


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
        return cityMapper.mapToDto(cityService.save(city, true));
    }

    public CityDto merge(CityMergeDto cityDto) {
        Account current = accountService.findById(SecurityUtil.getCurrentAccountId());
        if(current.getAccountType() != AccountType.SYSTEM) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no privileges to perform that action");
        }
        return cityMapper.mapToDto(cityService.merge(cityDto));
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

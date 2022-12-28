package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Address;
import org.springframework.stereotype.Component;

@Component
public record AddressMapper(
        CityMapper cityMapper
) implements Mapper<Address, AddressDto> {

    @Override
    public AddressDto mapToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .cityDto(cityMapper.mapToDto(address.getCity()))
                .streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber())
                .build();
    }

    @Override
    public Address mapToEntity(AddressDto addressDto) {
        return Address.builder()
                .id(addressDto.getId())
                .city(cityMapper.mapToEntity(addressDto.getCityDto()))
                .streetName(addressDto.getStreetName())
                .streetNumber(addressDto.getStreetNumber())
                .build();
    }
}

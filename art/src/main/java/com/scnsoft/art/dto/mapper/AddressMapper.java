package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.entity.Address;

@Component
public record AddressMapper(
    CityMapper cityMapper
) {
    public AddressDto mapToDto(Address address) {
        return AddressDto.builder()
            .id(address.getId())
            .cityDto(cityMapper.mapToDto(address.getCity()))
            .streetName(address.getStreetName())
            .streetNumber(address.getStreetNumber())
            .build();
    }

    public Address mapToEntity(AddressDto addressDto) {
        return Address.builder()
            .id(addressDto.getId())
            .city(cityMapper.mapToEntity(addressDto.getCityDto()))
            .streetName(addressDto.getStreetName())
            .streetNumber(addressDto.getStreetNumber())
            .build();
    }
}

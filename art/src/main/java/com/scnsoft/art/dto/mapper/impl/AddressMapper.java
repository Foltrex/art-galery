package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public record AddressMapper(CityMapper cityMapper) implements Mapper<Address, AddressDto> {

    @Override
    public AddressDto mapToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .city(cityMapper.mapToDto(address.getCity()))
                .fullName(address.getFullName())
                .build();
    }

    @Override
    public Address mapToEntity(AddressDto addressDto) {
        return Address.builder()
                .id(addressDto.getId())
                .city(cityMapper.mapToEntity(addressDto.getCity()))
                .fullName(addressDto.getFullName())
                .build();
    }

    public Page<AddressDto> mapPageToDto(final Page<Address> addressPage) {
        return new PageImpl<>(addressPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), addressPage.getPageable(), addressPage.getTotalElements());
    }

}

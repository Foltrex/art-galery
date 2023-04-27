package com.scnsoft.user.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.user.dto.AddressDto;
import com.scnsoft.user.entity.Address;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface AddressMapper {

    AddressDto mapToDto(Address address);

    Address mapToEntity(AddressDto addressDto);
}

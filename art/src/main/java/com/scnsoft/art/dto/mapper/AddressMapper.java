package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.entity.Address;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public abstract class AddressMapper {

    public abstract AddressDto mapToDto(Address address);

    public abstract Address mapToEntity(AddressDto addressDto);

    public Page<AddressDto> mapPageToDto(final Page<Address> addressPage) {
        List<AddressDto> addressDtos = addressPage.stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = addressPage.getPageable();
        long totalElements = addressPage.getTotalElements();

        return new PageImpl<>(addressDtos, pageable, totalElements);
    }

}

package com.scnsoft.art.dto.mapper;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.entity.Address;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public abstract class AddressMapper {

    public abstract AddressDto mapToDto(Address address);

    public abstract Address mapToEntity(AddressDto addressDto);

    public Page<AddressDto> mapPageToDto(final Page<Address> addressPage) {
        return addressPage.map(this::mapToDto);
    }

}

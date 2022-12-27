package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationMapper implements Mapper<Organization, OrganizationDto> {

    private final AddressMapper addressMapper;

    @Override
    public OrganizationDto mapToDto(Organization organization) {
        return OrganizationDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .address(mapAddressToDto(organization.getAddress()))
                .status(organization.getStatus())
                .build();
    }

    @Override
    public Organization mapToEntity(OrganizationDto organizationDto) {
        return Organization.builder()
                .id(organizationDto.getId())
                .name(organizationDto.getName())
                .address(mapAddressDtoToEntity(organizationDto.getAddress()))
                .status(organizationDto.getStatus())
                .build();
    }

    private AddressDto mapAddressToDto(Address address) {
        return address != null ? addressMapper.mapToDto(address) : null;
    }

    private Address mapAddressDtoToEntity(AddressDto addressDto) {
        return addressDto != null ? addressMapper.mapToEntity(addressDto) : null;
    }

}

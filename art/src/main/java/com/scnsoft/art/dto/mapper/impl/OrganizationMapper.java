package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                .status(mapStatusToDto(organization.getStatus()))
                .build();
    }

    @Override
    public Organization mapToEntity(OrganizationDto organizationDto) {
        return organizationDto != null
                ? Organization.builder()
                .id(organizationDto.getId())
                .name(organizationDto.getName())
                .address(mapAddressDtoToEntity(organizationDto.getAddress()))
                .status(mapStatusToEntity(organizationDto.getStatus()))
                .build()
                : null;
    }

    public Page<OrganizationDto> mapPageToDto(final Page<Organization> organizationPage) {
        return new PageImpl<>(organizationPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), organizationPage.getPageable(), organizationPage.getTotalElements());

    }

    private AddressDto mapAddressToDto(Address address) {
        return address != null ? addressMapper.mapToDto(address) : null;
    }

    private Address mapAddressDtoToEntity(AddressDto addressDto) {
        return addressDto != null ? addressMapper.mapToEntity(addressDto) : null;
    }

    private Organization.Status mapStatusToEntity(String status) {
        return status != null ? Organization.Status.valueOf(status) : null;
    }

    private String mapStatusToDto(Organization.Status status) {
        return status != null ? status.toString() : null;
    }

}

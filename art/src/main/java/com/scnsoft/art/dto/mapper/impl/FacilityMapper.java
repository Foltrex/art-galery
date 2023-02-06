package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public record FacilityMapper(
        AddressMapper addressMapper,
        OrganizationMapper organizationMapper
) implements Mapper<Facility, FacilityDto> {

    @Override
    public FacilityDto mapToDto(Facility facility) {
        return facility != null
                ? FacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .isActive(facility.getIsActive())
                .address(facility.getAddress() != null ? addressMapper.mapToDto(facility.getAddress()) : null)
                .organization(organizationMapper.mapToDto(facility.getOrganization()))
                .build()
                : null;
    }

    @Override
    public Facility mapToEntity(FacilityDto facilityDto) {
        return facilityDto != null
                ? Facility.builder()
                .id(facilityDto.getId())
                .name(facilityDto.getName())
                .isActive(facilityDto.getIsActive())
                .address(facilityDto.getAddress() != null ? addressMapper.mapToEntity(facilityDto.getAddress()) : null)
                .organization(organizationMapper.mapToEntity(facilityDto.getOrganization()))
                .build()
                : null;
    }

    public Page<FacilityDto> mapPageToDto(final Page<Facility> facilityPage) {
        return new PageImpl<>(facilityPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), facilityPage.getPageable(), facilityPage.getTotalElements());

    }
}

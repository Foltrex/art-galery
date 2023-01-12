package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Facility;
import org.springframework.stereotype.Component;

@Component
public record FacilityMapper(
        AddressMapper addressMapper
) implements Mapper<Facility, FacilityDto> {

    @Override
    public FacilityDto mapToDto(Facility facility) {
        return FacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .isActive(facility.getIsActive())
                .address(facility.getAddress() != null ? addressMapper.mapToDto(facility.getAddress()) : null)
                .build();
    }

    @Override
    public Facility mapToEntity(FacilityDto facilityDto) {
        return Facility.builder()
                .id(facilityDto.getId())
                .name(facilityDto.getName())
                .isActive(facilityDto.getIsActive())
                .address(facilityDto.getAddress() != null ? addressMapper.mapToEntity(facilityDto.getAddress()) : null)
                .build();
    }
}

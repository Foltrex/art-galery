package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.entity.Facility;

@Component
public record FacilityMapper(
    AddressMapper addressMapper
) {
    
    public FacilityDto mapToDto(Facility facility) {
        return FacilityDto.builder()
            .id(facility.getId())
            .name(facility.getName())
            .isActive(facility.getIsActive())
            .addressDto(addressMapper.mapToDto(facility.getAddress()))
            .build();
    }

    public Facility mapToEntity(FacilityDto facilityDto) {
        return Facility.builder()
            .id(facilityDto.getId())
            .name(facilityDto.getName())
            .isActive(facilityDto.getIsActive())
            .address(addressMapper.mapToEntity(facilityDto.getAddressDto()))
            .build();
    }
}

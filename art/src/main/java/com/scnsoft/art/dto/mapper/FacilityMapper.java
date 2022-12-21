package com.scnsoft.art.dto.mapper;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.entity.Facility;

@Component
public class FacilityMapper {
    
    public FacilityDto mapToDto(Facility facility) {
        return FacilityDto.builder()
            .id(facility.getId())
            .name(facility.getName())
            .isActive(facility.isActive())
            .cityId(facility.getCityId())
            .build();
    }

    public Facility mapToEntity(FacilityDto facilityDto) {
        return Facility.builder()
            .id(facilityDto.id())
            .name(facilityDto.name())
            .isActive(facilityDto.isActive())
            .cityId(facilityDto.cityId())
            .build();
    }
}

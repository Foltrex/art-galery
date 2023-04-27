package com.scnsoft.art.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.entity.Facility;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class FacilityMapper {
    @Mapping(
            target = "organizationId",
            source = "facility.organization.id"
    )
    @Mapping(
            target = "organizationName",
            source = "facility.organization.name"
    )
    public abstract FacilityDto mapToDto(Facility facility);

    @Mapping(
            target = "organization.id",
            source = "organizationId"
    )
    @Mapping(
            target = "organization.name",
            source = "organizationName"
    )
    public abstract Facility mapToEntity(FacilityDto facilityDto);

    public Page<FacilityDto> mapPageToDto(Page<Facility> facilityPage) {
        List<FacilityDto> facilityDtos = facilityPage.stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = facilityPage.getPageable();
        long totalElements = facilityPage.getTotalElements();
        return new PageImpl<>(facilityDtos, pageable, totalElements);

    }
}

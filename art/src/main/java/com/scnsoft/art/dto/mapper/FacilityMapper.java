package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.entity.Facility;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, OrganizationMapper.class})
public abstract class FacilityMapper {

    public abstract FacilityDto mapToDto(Facility facility);

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

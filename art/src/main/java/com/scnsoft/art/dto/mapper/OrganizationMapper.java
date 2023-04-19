package com.scnsoft.art.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class OrganizationMapper {

    @Mapping(
        source = "organization.facilities", 
        target = "facilities", 
        qualifiedByName = "mapToDtoList"
    )
    public abstract OrganizationDto mapToDto(Organization organization);


    public abstract Organization mapToEntity(OrganizationDto organizationDto);

    public Page<OrganizationDto> mapPageToDto(final Page<Organization> organizationPage) {
        List<OrganizationDto> organizationDtos = organizationPage.stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = organizationPage.getPageable();
        long totalElements = organizationPage.getTotalElements();
        return new PageImpl<>(organizationDtos, pageable, totalElements);

    }

    @Named("mapToDtoList")
    public List<FacilityDto> mapToDtoList(List<Facility> facilities) {
        return facilities.stream()
            .map(facility -> {
                return FacilityDto.builder()
                    .id(facility.getId())
                    .name(facility.getName())
                    .isActive(facility.getIsActive())
                    .build();
            })
            .toList();
    }

    public abstract List<Facility> mapToEntityList(List<FacilityDto> facilityDtos);
}

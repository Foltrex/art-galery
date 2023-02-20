package com.scnsoft.art.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.entity.Organization;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public abstract class OrganizationMapper {

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
}

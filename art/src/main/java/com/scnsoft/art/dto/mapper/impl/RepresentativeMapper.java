package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.OrganizationDto;
import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Representative;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RepresentativeMapper implements Mapper<Representative, RepresentativeDto> {

    private final FacilityMapper facilityMapper;
    private final OrganizationMapper organizationMapper;

    @Override
    public RepresentativeDto mapToDto(Representative representative) {
        return RepresentativeDto.builder()
                .id(representative.getId())
                .facility(mapFacilityToDto(representative.getFacility()))
                .organization(mapOrganizationToDto(representative.getOrganization()))
                .organizationRole(representative.getOrganizationRole())
                .accountId(representative.getAccountId())
                .build();
    }

    @Override
    public Representative mapToEntity(RepresentativeDto representativeDto) {
        return Representative.builder()
                .id(representativeDto.getId())
                .facility(mapFacilityDtoToEntity(representativeDto.getFacility()))
                .organization(mapOrganizationDtoToEntity(representativeDto.getOrganization()))
                .organizationRole(representativeDto.getOrganizationRole())
                .accountId(representativeDto.getAccountId())
                .build();
    }

    public Page<RepresentativeDto> mapPageToDto(final Page<Representative> representativesPage) {
        return new PageImpl<>(representativesPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), representativesPage.getPageable(), representativesPage.getTotalElements());

    }

    private FacilityDto mapFacilityToDto(Facility facility) {
        return facility != null ? facilityMapper.mapToDto(facility) : null;
    }

    private OrganizationDto mapOrganizationToDto(Organization organization) {
        return organization != null ? organizationMapper.mapToDto(organization) : null;
    }

    private Facility mapFacilityDtoToEntity(FacilityDto facilityDto) {
        return facilityDto != null ? facilityMapper.mapToEntity(facilityDto) : null;
    }

    private Organization mapOrganizationDtoToEntity(OrganizationDto organizationDto) {
        return organizationDto != null ? organizationMapper.mapToEntity(organizationDto) : null;
    }
}

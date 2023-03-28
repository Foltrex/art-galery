package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.entity.Representative;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FacilityMapper.class, OrganizationMapper.class,
        OrganizationRoleMapper.class})
public abstract class RepresentativeMapper {

    public abstract RepresentativeDto mapToDto(Representative representative);

    public abstract Representative mapToEntity(RepresentativeDto representativeDto);

    public Page<RepresentativeDto> mapPageToDto(final Page<Representative> representativesPage) {
        List<RepresentativeDto> representativeDtos = representativesPage.stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = representativesPage.getPageable();
        long totalElements = representativesPage.getTotalElements();
        return new PageImpl<>(representativeDtos, pageable, totalElements);
    }
}

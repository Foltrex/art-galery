package com.scnsoft.art.facade;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.impl.FacilityMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.service.FacilityService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FacilityServiceFacade {
    
    private final FacilityService facilityService;
    private final FacilityMapper facilityMapper;


    public Page<FacilityDto> findAll(Pageable pageable) {
        return facilityMapper.mapPageToDto(facilityService.findAll(pageable));
    }

    public List<FacilityDto> findAll() {
        return facilityService.findAll()
            .stream()
            .map(facilityMapper::mapToDto)
            .toList();
    }

    public Page<FacilityDto> findAllByOrganizationId(UUID organizationId, Pageable pageable) {
        return facilityMapper.mapPageToDto(facilityService.findAllByOrganizationId(organizationId, pageable));
    }

    public FacilityDto findById(UUID id) {
        return facilityMapper.mapToDto(facilityService.findById(id));
    }

    public FacilityDto create(FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        return facilityMapper.mapToDto(facilityService.save(facility));
    }

    public FacilityDto update(UUID id, FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        return facilityMapper.mapToDto(facilityService.updateById(id, facility));
    }

    public Void delete(@PathVariable UUID id) {
        facilityService.deleteById(id);
        return null;
    }
}

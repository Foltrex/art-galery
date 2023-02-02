package com.scnsoft.art.facade;

import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.mapper.impl.FacilityMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

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

    public FacilityDto save(FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        return facilityMapper.mapToDto(facilityService.save(facility));
    }

    public FacilityDto updateById(UUID id, FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);
        return facilityMapper.mapToDto(facilityService.updateById(id, facility));
    }

    public Void deleteById(@PathVariable UUID id) {
        facilityService.deleteById(id);
        return null;
    }

    public Page<FacilityDto> findAllByAccountId(UUID accountId, Pageable pageable) {
        return facilityMapper.mapPageToDto(facilityService.findAllByAccountId(accountId, pageable));
    }

    public List<FacilityDto> findAllByAccountId(UUID accountId) {
        return facilityService.findAllByAccountId(accountId)
            .stream()
            .map(facilityMapper::mapToDto)
            .toList();
    }
}

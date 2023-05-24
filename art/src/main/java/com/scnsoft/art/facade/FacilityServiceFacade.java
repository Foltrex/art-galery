package com.scnsoft.art.facade;

import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.FacilityDto;
import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.dto.mapper.FacilityMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.FileService;
import com.scnsoft.art.service.OrganizationService;
import com.scnsoft.art.service.user.AccountService;
import com.scnsoft.art.service.user.MetadataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FacilityServiceFacade {

    private final FacilityService facilityService;
    private final FileService fileService;
    private final FacilityMapper facilityMapper;
    private final AccountService accountService;
    private final OrganizationService organizationService;
    private final MetadataServiceImpl metadataService;


    public Page<FacilityDto> findAll(Pageable pageable, FacilityFilter filter) {
        var page = facilityService.findAll(pageable, filter);
        Page<FacilityDto> result = facilityMapper.mapPageToDto(page);
        result.forEach(f -> f.setImages(fileService.findAllByEntityId(f.getId())));
        return result;
    }

    public List<FacilityDto> findAll(
        FacilityFilter filter
    ) {
        return facilityService.findAll(filter)
                .stream()
                .map(facilityMapper::mapToDto)
                .toList();
    }

    public FacilityDto findById(UUID id) {
        FacilityDto dto = facilityMapper.mapToDto(facilityService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facility with id " + id + " not found")));
        dto.setImages(fileService.findAllByEntityId(id));
        return dto;
    }

    public FacilityDto save(FacilityDto facilityDto) {
        Facility facility = facilityMapper.mapToEntity(facilityDto);

        UUID currentAccountId = SecurityUtil.getCurrentAccountId();
        Account accountDto = accountService.findById(currentAccountId);

        Organization organization;
        if(accountDto.getAccountType() == AccountType.SYSTEM) {
            organization = organizationService.findById(facility.getOrganization().getId());
        } else {
            Metadata orgId = metadataService.findByKeyAndAccountId(
                    MetadataEnum.ORGANIZATION_ID.getValue(),
                    currentAccountId);
            organization = organizationService.findById(UUID.fromString(orgId.getValue()));
        }
        facility.setOrganization(organization);

        return facilityMapper.mapToDto(facilityService.save(facility, facilityDto.getImages()));
    }

    public Void deleteById(@PathVariable UUID id) {
        facilityService.deleteById(id);
        return null;
    }

}

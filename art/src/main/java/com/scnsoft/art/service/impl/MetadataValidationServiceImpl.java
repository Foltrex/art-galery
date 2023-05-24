package com.scnsoft.art.service.impl;

import com.scnsoft.art.dto.AccountDto;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.MetaDataDto;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.dto.OrganizationRole;
import com.scnsoft.art.dto.mapper.AccountMapper;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FacilityService;
import com.scnsoft.art.service.MetadataValidationService;
import com.scnsoft.art.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataValidationServiceImpl implements MetadataValidationService {

    private final AccountMapper accountMapper;
    private final AccountService accountFeignClient;
    private final FacilityService facilityService;

    @Override
    public boolean validate(AccountDto accountDto, UUID allowedOrgId) {
        if(accountDto.getMetadata() == null) {
            accountDto.setMetadata(new HashSet<>());
        }

        Optional<AccountDto> currentUser = getCurrent();

        switch (accountDto.getAccountType()) {
            case SYSTEM -> {
                if(currentUser.isEmpty() || !AccountType.SYSTEM.equals(currentUser.get().getAccountType())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only system user can create system user");
                }
                for(MetaDataDto meta : accountDto.getMetadata()) {
                    if(notAllowed(meta.getKey(),
                            MetadataEnum.ORGANIZATION_ROLE,
                            MetadataEnum.ORGANIZATION_ID,
                            MetadataEnum.FACILITY_ID,
                            MetadataEnum.CITY_ID)) {
                        return false;
                    }
                }
                return true;
            }
            case ARTIST -> {
                for(MetaDataDto meta : accountDto.getMetadata()) {
                    if (notAllowed(meta.getKey(),
                            MetadataEnum.ORGANIZATION_ROLE,
                            MetadataEnum.ORGANIZATION_ID,
                            MetadataEnum.FACILITY_ID)) {
                        return false;
                    }
                }
                return true;
            }
            case REPRESENTATIVE -> {
                String role = required(accountDto.getMetadata(), MetadataEnum.ORGANIZATION_ROLE)
                        .map(MetaDataDto::getValue)
                        .orElse(null);
                if(!canCreateRepresentative(currentUser, accountDto, allowedOrgId)) {
                    return false;
                }
                UUID organizationId = defineOrganizationId(currentUser, accountDto, allowedOrgId);
                if(organizationId == null) {
                    log.error("Failed to register representative, allowed organization id is not defined");
                    return false;
                }
                UUID currentOrgId = required(accountDto.getMetadata(), MetadataEnum.ORGANIZATION_ID)
                        .map(v -> UUID.fromString(v.getValue()))
                        .orElse(null);
                if(!organizationId.equals(currentOrgId)) {
                    log.error("Failed to register representative, organization id is not valid: {} -> {}", organizationId, currentOrgId);
                    return false;
                }
                if(OrganizationRole.MEMBER.name().equals(role)) {
                    UUID facilityId = required(accountDto.getMetadata(), MetadataEnum.FACILITY_ID)
                            .map(v -> UUID.fromString(v.getValue()))
                            .orElse(null);
                    if(facilityId == null) {
                        log.error("Failed to register representative, no facility id");
                        return false;
                    }
                    Optional<Facility> facility = facilityService.findById(facilityId);
                    if(facility.isEmpty() || facility.get().getOrganization() == null || !facility.get().getOrganization().getId().equals(organizationId)) {
                        log.error("Failed to register representative, facility is invalid");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private UUID defineOrganizationId(Optional<AccountDto> currentUser, AccountDto accountDto, UUID allowedOrgId) {
        if(currentUser.isPresent()) {
            if(AccountType.SYSTEM.equals(currentUser.get().getAccountType())) {
                return required(accountDto.getMetadata(), MetadataEnum.ORGANIZATION_ID)
                        .map(v -> UUID.fromString(v.getValue()))
                        .orElse(null);
            } else {
                return required(currentUser.get().getMetadata(), MetadataEnum.ORGANIZATION_ID)
                        .map(v -> UUID.fromString(v.getValue()))
                        .orElse(null);
            }
        } else {
            return allowedOrgId;
        }
    }

    private boolean canCreateRepresentative(Optional<AccountDto> currentUser, AccountDto accountDto, UUID allowedOrganization) {
        if(currentUser.isPresent()) {
            AccountDto logged = currentUser.get();
            switch (logged.getAccountType()) {
                case SYSTEM -> {return true;}
                case REPRESENTATIVE -> {
                    OrganizationRole authorisedRole = required(logged.getMetadata(), MetadataEnum.ORGANIZATION_ROLE)
                            .map(v -> OrganizationRole.fromString(v.getValue()))
                            .orElse(OrganizationRole.UNKNOWN);
                    OrganizationRole creationRole = required(accountDto.getMetadata(), MetadataEnum.ORGANIZATION_ROLE)
                            .map(v -> OrganizationRole.fromString(v.getValue()))
                            .orElse(OrganizationRole.UNKNOWN);
                    if(authorisedRole.getStrength() - creationRole.getStrength() >= 0) {
                        return true;
                    } else {
                        log.error("Failed to validate representative," +
                                " current representative user have no permissions to create such kind of users");
                        return false;
                    }
                }
                default -> {
                    log.error("Failed to validate representative, current logged user is not system and not representative");
                    return false;
                }
            }
        } else {
            var creationRole = required(accountDto.getMetadata(), MetadataEnum.ORGANIZATION_ROLE)
                    .map(v -> OrganizationRole.fromString(v.getValue()))
                    .orElse(OrganizationRole.UNKNOWN);
            if(!OrganizationRole.CREATOR.equals(creationRole)) {
                log.error("Failed to create representative, unauthorised users may create only organization creators");
                return false;
            }
            if(allowedOrganization == null) {
                log.error("Failed to create representative, allowed facility is null");
                return false;
            }
            return true;
        }

    }

    private Optional<AccountDto> getCurrent() {
        UUID current = SecurityUtil.getCurrentAccountId();
        if(current == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(accountMapper.mapToDto(accountFeignClient.findById(current)));
        } catch (Exception e) {
            log.error("Failed to find current user, id is " + current);
            return Optional.empty();
        }
    }

    private Optional<MetaDataDto> required(Set<MetaDataDto> metaDataList, MetadataEnum value) {
        for(MetaDataDto m : metaDataList) {
            if(value.getValue().equals(m.getKey())) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    private boolean notAllowed(String key, MetadataEnum ... values) {
        for(MetadataEnum v : values) {
            if(v.getValue().equals(key)) {
                return true;
            }
        }
        return false;
    }
}

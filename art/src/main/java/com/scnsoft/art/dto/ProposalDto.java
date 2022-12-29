package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProposalDto {
    private UUID id;
    private BigDecimal price;
    private double commission;
    private long currency;
    private ArtistDto artistDto;
    private OrganizationDto organizationDto;
    private FacilityDto facilityDto;
    private UpdateSide updateSide;

    public enum UpdateSide {
        ARTIST, ORGANIZATION
    }
}

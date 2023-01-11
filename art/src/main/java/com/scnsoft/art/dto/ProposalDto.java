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
    // TODO: price for one?
    private BigDecimal price;
    private double commission;
    private long currency;
    private ArtistDto artist;
    private OrganizationDto organization;
    private FacilityDto facility;
    private UpdateSide updateSide;

    public enum UpdateSide {
        ARTIST, ORGANIZATION
    }
}

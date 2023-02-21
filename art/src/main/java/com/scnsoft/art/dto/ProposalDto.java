package com.scnsoft.art.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {
    private UUID id;
    // TODO: price for one?
    private BigDecimal price;
    private double commission;
    private ArtDto art;
    private CurrencyDto currency;
    private ArtistDto artist;
    private OrganizationDto organization;
    private FacilityDto facility;
    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
}

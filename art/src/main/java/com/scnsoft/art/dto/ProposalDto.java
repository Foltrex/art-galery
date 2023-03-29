package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {
    private UUID id;
    private BigDecimal price;
    private double commission;
    private ArtDto art;
    private CurrencyDto currency;
    private UUID artistAccountId;
    private OrganizationDto organization;
    private FacilityDto facility;
    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
}

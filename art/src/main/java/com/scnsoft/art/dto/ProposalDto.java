package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {
    private UUID id;
    private UUID accountId;
    private UUID updateAccountId;
    private BigDecimal price;
    private double commission;
    private ArtDto art;
    private CurrencyDto currency;
    private Proposal.ProposalStatus status;
    private UUID artistAccountId;
    private OrganizationDto organization;
    private List<FacilityDto> facilities;
    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
    private Boolean exhibited;
    private List<Map<String, Object>> audit;
}

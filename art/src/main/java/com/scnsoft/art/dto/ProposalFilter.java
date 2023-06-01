package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProposalFilter {
    private UUID accountId;
    private UUID facilityId;
    private UUID organizationId;
}

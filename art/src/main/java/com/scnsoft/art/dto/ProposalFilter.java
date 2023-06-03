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
    /**
    * 1 - receieved
    * 0 - approved
    * -1 - sent
    */
    private Integer received;
}

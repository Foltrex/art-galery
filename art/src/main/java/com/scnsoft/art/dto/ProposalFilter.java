package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Proposal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProposalFilter {
    private UUID accountId;
    private UUID facilityId;
    private UUID organizationId;
    private List<Proposal.ProposalStatus> status;
    /**
    * 1 - receieved
    * 0 - approved
    * -1 - sent
    */
    private Integer received;
}

package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class ProposalBook {
    Page<ProposalDto> sent;
    Page<ProposalDto> received;
    Page<ProposalDto> approved;
}

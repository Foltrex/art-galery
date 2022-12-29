package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalMapper {
    private final ArtistMapper artistMapper;

    public ProposalDto mapToDto(Proposal proposal) {
        // return ProposalDto.builder()
        //     .id(proposal.getId())
        //     .price(proposal.getPrice())
        //     .commission(proposal.getCommission())
        //     .currency(proposal.getCommission())
        //     .currency(proposal.getCurrency())
        //     .
        //     .build();
        return null;
    }

    public Proposal mapToEntity(ProposalDto proposalDto) {
        return Proposal.builder()
                .id(proposalDto.getId())
                .price(proposalDto.getPrice())
                .commission(proposalDto.getCommission())
                .currency(proposalDto.getCurrency())
                // .
                .build();
    }
}

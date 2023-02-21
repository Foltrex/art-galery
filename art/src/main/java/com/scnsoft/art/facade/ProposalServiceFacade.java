package com.scnsoft.art.facade;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.impl.ProposalServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProposalServiceFacade {
    private final ProposalServiceImpl proposalService;
    private final ProposalMapper proposalMapper;

    public ProposalDto save(ProposalDto proposalDto) {
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        return proposalMapper.mapToDto(proposalService.save(proposal));
    }
}

package com.scnsoft.art.facade;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProposalServiceFacade {
    private final ProposalServiceImpl proposalService;
    private final ProposalMapper proposalMapper;

    public ProposalDto save(ProposalDto proposalDto) {
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        return proposalMapper.mapToDto(proposalService.save(proposal));
    }

    public Page<ProposalDto> findAllByAccountId(UUID accountId, Pageable pageable) {
        return proposalMapper.mapPageToDto(proposalService.findAllByAccountId(accountId, pageable));
    }

    public long countByAccountId(UUID accountId) {
        return proposalService.countByAccountId(accountId);
    }

    public Void deleteById(UUID id) {
        proposalService.deleteById(id);
        return null;
    }
}

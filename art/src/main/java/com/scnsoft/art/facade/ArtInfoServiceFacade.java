package com.scnsoft.art.facade;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.impl.ArtInfoServiceImpl;
import com.scnsoft.art.service.impl.ProposalServiceImpl;

@Component
public record ArtInfoServiceFacade(
    ArtInfoServiceImpl artInfoServiceImpl,
    ArtInfoMapper artInfoMapper,
    ProposalServiceImpl proposalServiceImpl,
    ProposalMapper proposalMapper
) {
    public ArtInfoDto findByArtId(UUID artId) {
        return artInfoMapper.mapToDto(artInfoServiceImpl.findByArtId(artId));
    }

    public ArtInfoDto save(ArtInfoDto artInfoDto) {
        ArtInfo artInfo = artInfoMapper.mapToEntity(artInfoDto);
        return artInfoMapper.mapToDto(artInfoServiceImpl.save(artInfo));
    }

    public ArtInfoDto createFromProposal(ProposalDto proposalDto) {
        ArtInfo artInfo = artInfoMapper.mapProposalDtoToEntity(proposalDto);
        Proposal proposal = proposalMapper.mapToEntity(proposalDto);
        proposalServiceImpl.discardAllProposalsForArtExceptPassed(artInfo.getArt(), proposal);
        return artInfoMapper.mapToDto(artInfoServiceImpl.save(artInfo));
    }
}

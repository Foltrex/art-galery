package com.scnsoft.art.facade;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.impl.ArtInfoServiceImpl;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public record ArtInfoServiceFacade(
        ArtInfoServiceImpl artInfoServiceImpl,
        ArtInfoMapper artInfoMapper,
        ProposalServiceImpl proposalServiceImpl,
        ProposalMapper proposalMapper
) {
    public List<ArtInfoDto> findByArtId(UUID artId) {
        return artInfoServiceImpl.findByArtId(artId)
                .stream()
                .map(artInfoMapper::mapToDto)
                .toList();
    }

    public Optional<ArtInfoDto> findLastByArtId(UUID artId) {
        return artInfoServiceImpl.findLastByArtId(artId)
                .map(artInfoMapper::mapToDto);
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

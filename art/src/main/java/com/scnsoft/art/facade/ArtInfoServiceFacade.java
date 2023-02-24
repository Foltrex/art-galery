package com.scnsoft.art.facade;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.service.impl.ArtInfoServiceImpl;

@Component
public record ArtInfoServiceFacade(
    ArtInfoServiceImpl artInfoServiceImpl,
    ArtInfoMapper artInfoMapper
) {
    public ArtInfoDto save(ArtInfoDto artInfoDto) {
        ArtInfo artInfo = artInfoMapper.mapToEntity(artInfoDto);
        return artInfoMapper.mapToDto(artInfoServiceImpl.save(artInfo));
    }

    public ArtInfoDto createFromProposal(ProposalDto proposalDto) {
        ArtInfo artInfo = artInfoMapper.mapProposalDtoToEntity(proposalDto);
        return artInfoMapper.mapToDto(artInfoServiceImpl.save(artInfo));
    }
}

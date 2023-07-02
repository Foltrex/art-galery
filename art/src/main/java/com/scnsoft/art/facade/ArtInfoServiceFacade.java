package com.scnsoft.art.facade;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.dto.mapper.ProposalMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.service.ArtInfoService;
import com.scnsoft.art.service.impl.ProposalServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public record ArtInfoServiceFacade(
        ArtInfoService artInfoService,
        ArtInfoMapper artInfoMapper,
        ProposalServiceImpl proposalServiceImpl,
        ProposalMapper proposalMapper
) {
    public List<ArtInfoDto> findByArtId(UUID artId) {
        return artInfoService.findByArtId(artId)
                .stream()
                .map(artInfoMapper::mapToDto)
                .toList();
    }

    public ArtInfoDto save(ArtInfoDto artInfoDto) {
        ArtInfo artInfo = artInfoMapper.mapToEntity(artInfoDto);
        return artInfoMapper.mapToDto(artInfoService.save(artInfo));
    }

}

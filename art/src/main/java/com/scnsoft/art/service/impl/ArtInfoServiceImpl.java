package com.scnsoft.art.service.impl;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.repository.ProposalRepository;
import com.scnsoft.art.service.ArtInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ArtInfoServiceImpl implements ArtInfoService {

    private final ArtInfoRepository artInfoRepository;
    private final ProposalRepository proposalRepository;
    private final ArtInfoMapper artInfoMapper;

    public ArtInfoDto create(ArtInfoDto artInfoDto) {
        ArtInfo artInfo = artInfoMapper.mapToEntity(artInfoDto);
        Proposal proposal = proposalRepository
                .findById(artInfoDto.getProposalId())
                .orElseThrow(() -> new ArtResourceNotFoundException("Proposal not found by id: " + artInfoDto.getProposalId()));

        artInfo.setOrganization(proposal.getOrganization());
        artInfo.setFacility(proposal.getFacility());
        artInfo.setPrice(proposal.getPrice());
        artInfo.setCommission(proposal.getCommission());
        artInfo.setCreationDate(new Date());

        ArtInfo persistedArtInfo = artInfoRepository.save(artInfo);
        return artInfoMapper.mapToDto(persistedArtInfo);
    }

}

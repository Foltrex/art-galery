package com.scnsoft.art.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.repository.ProposalRepository;
import com.scnsoft.art.service.ArtInfoService;

@Service
public record ArtInfoServiceImpl(
    ArtInfoRepository artInfoRepository,
    ProposalRepository proposalRepository
) implements ArtInfoService {

    public ArtInfo findByArtId(UUID artId) {
        return artInfoRepository.findByArtId(artId)
            .orElseThrow(ArtResourceNotFoundException::new);
    }

    public ArtInfo save(ArtInfo artInfo) {
        if (artInfo.getExpositionDateStart() == null) {
            Date now = new Date();
            artInfo.setExpositionDateStart(now);
        }

        return artInfoRepository.save(artInfo);
    }
}

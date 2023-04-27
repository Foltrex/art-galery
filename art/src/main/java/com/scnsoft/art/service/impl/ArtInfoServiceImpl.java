package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.repository.ProposalRepository;
import com.scnsoft.art.service.ArtInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public record ArtInfoServiceImpl(
        ArtInfoRepository artInfoRepository,
        ProposalRepository proposalRepository
) implements ArtInfoService {

    public List<ArtInfo> findByArtId(UUID artId) {
        return artInfoRepository.findByArtId(artId);
    }

    public Optional<ArtInfo> findLastByArtId(UUID artId) {
        return artInfoRepository.findLastByArtId(artId);
    }

    public ArtInfo save(ArtInfo artInfo) {
        if (artInfo.getExpositionDateStart() == null) {
            Date now = new Date();
            artInfo.setExpositionDateStart(now);
        }

        return artInfoRepository.save(artInfo);
    }
}

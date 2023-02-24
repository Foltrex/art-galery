package com.scnsoft.art.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.repository.ArtInfoRepository;
import com.scnsoft.art.service.ArtInfoService;

@Service
public record ArtInfoServiceImpl(ArtInfoRepository artInfoRepository) implements ArtInfoService {

    public ArtInfo save(ArtInfo artInfo) {
        if (artInfo.getExpositionDateStart() == null) {
            Date now = new Date();
            artInfo.setExpositionDateStart(now);
        }

        return artInfoRepository.save(artInfo);
    }
}

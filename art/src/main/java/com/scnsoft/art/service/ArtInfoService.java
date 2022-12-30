package com.scnsoft.art.service;

import org.joda.time.Instant;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.repository.ArtInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtInfoService {

    private final ArtInfoRepository artInfoRepository;

    public ArtInfo create(Proposal proposal) {
        // ArtInfo artInfo = ArtInfo.builder()
            
        //     .build();
        // TODO: write implementation
        ArtInfo artInfo = ArtInfo.builder()
            .creationDate(Instant.now().toDate())
            .build();
        return null;
    }

}

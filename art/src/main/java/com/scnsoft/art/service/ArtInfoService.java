package com.scnsoft.art.service;

import com.scnsoft.art.entity.ArtInfo;

import java.util.List;
import java.util.UUID;

public interface ArtInfoService {
    List<ArtInfo> findByArtId(UUID artId);
    ArtInfo save(ArtInfo artInfo);

    List<ArtInfo> findLastByArtIds(List<UUID> artIds);
}

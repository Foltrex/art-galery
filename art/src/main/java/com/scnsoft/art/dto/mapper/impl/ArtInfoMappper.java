package com.scnsoft.art.dto.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.discovery.converters.Auto;
import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.ArtInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtInfoMappper implements Mapper<ArtInfo, ArtInfoDto> {

    @Autowired
    private ArtMapper artMapper;

    @Override
    public ArtInfoDto mapToDto(ArtInfo artInfo) {
        return ArtInfoDto.builder()
            .id(artInfo.getId())
            .artDto(artMapper.mapToDto(artInfo.getArt()))
            .expositionDateStart(artInfo.getExpositionDateStart())
            .expositionDateEnd(artInfo.getExpositionDateEnd())
            .status(
                com.scnsoft.art.dto.ArtInfoDto.Status.valueOf(artInfo.getStatus().name())
            )
            .build();
    }

    @Override
    public ArtInfo mapToEntity(ArtInfoDto artInfoDto) {
        return ArtInfo.builder()
            .id(artInfoDto.getId())
            .art(artMapper.mapToEntity(artInfoDto.getArtDto()))
            .expositionDateStart(artInfoDto.getExpositionDateStart())
            .expositionDateEnd(artInfoDto.getExpositionDateEnd())
            .status(
                com.scnsoft.art.entity.ArtInfo.Status.valueOf(artInfoDto.getStatus().name())
            )
            .build();
    }
    
}

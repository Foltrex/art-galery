package com.scnsoft.art.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.entity.ArtInfo;

@Mapper(componentModel = "spring", uses = {ArtMapper.class})
public abstract class ArtInfoMapper {

    // TODO: mapper for enum Status
    public abstract ArtInfoDto mapToDto(ArtInfo artInfo);

    public abstract ArtInfo mapToEntity(ArtInfoDto artInfoDto);

}

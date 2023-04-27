package com.scnsoft.art.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.ArtInfo;

@Mapper(componentModel = "spring", uses = {ArtMapper.class, FacilityMapper.class})
public interface ArtInfoMapper {

    @Mapping(
            target = "artId",
            source = "artInfo.art.id"
    )
    ArtInfoDto mapToDto(ArtInfo artInfo);

    ArtInfo mapToEntity(ArtInfoDto artInfoDto);

    @Mapping(target = "id", ignore = true)
    ArtInfo mapProposalDtoToEntity(ProposalDto proposal);
}

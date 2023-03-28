package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.ArtInfoDto;
import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.ArtInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArtMapper.class, FacilityMapper.class})
public interface ArtInfoMapper {

    ArtInfoDto mapToDto(ArtInfo artInfo);

    ArtInfo mapToEntity(ArtInfoDto artInfoDto);

    @Mapping(target = "id", ignore = true)
    ArtInfo mapProposalDtoToEntity(ProposalDto proposal);
}

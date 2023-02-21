package com.scnsoft.art.dto.mapper;

import org.mapstruct.Mapper;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.Proposal;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, OrganizationMapper.class, FacilityMapper.class, CurrencyMapper.class})
public interface ProposalMapper {

    ProposalDto mapToDto(Proposal proposal);

    Proposal mapToEntity(ProposalDto proposalDto);
}

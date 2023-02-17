package com.scnsoft.art.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.Proposal;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, OrganizationMapper.class, FacilityMapper.class})
public interface ProposalMapper {

    ProposalDto mapToDto(Proposal proposal);

    Proposal mapToEntity(ProposalDto proposalDto);

    @AfterMapping
    default void updateEntity(ProposalDto proposalDto, @MappingTarget Proposal proposal) {
        switch (proposalDto.getUpdateSide()) {
            case ARTIST: {
                proposal.setArtistConfirmation(true);
                proposal.setOrganizationConfirmation(null);
            }
            case ORGANIZATION: {
                proposal.setOrganizationConfirmation(true);
                proposal.setArtistConfirmation(null);
            }
        }
    }
}

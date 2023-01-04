package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalMapper {
    private final ArtistMapper artistMapper;
    private final OrganizationMapper organizationMapper;
    private final FacilityMapper facilityMapper;

    public ProposalDto mapToDto(Proposal proposal) {
        return proposal != null
            ? ProposalDto.builder()
                .id(proposal.getId())
                .price(proposal.getPrice())
                .commission(proposal.getCommission())
                .currency(proposal.getCurrency())
                .artistDto(artistMapper.mapToDto(proposal.getArtist()))
                .organizationDto(organizationMapper.mapToDto(proposal.getOrganization()))
                .facilityDto(facilityMapper.mapToDto(proposal.getFacility()))
                .build()
            : null;
    }

    public Proposal mapToEntity(ProposalDto proposalDto) {
        if (proposalDto == null) {
            return null;
        }

        Proposal.ProposalBuilder proposalBuilder = Proposal.builder()
            .id(proposalDto.getId())
            .artist(artistMapper.mapToEntity(proposalDto.getArtistDto()))
            .organization(organizationMapper.mapToEntity(proposalDto.getOrganizationDto()))
            .price(proposalDto.getPrice())
            .commission(proposalDto.getCommission())
            .currency(proposalDto.getCurrency());
        
        proposalBuilder = switch (proposalDto.getUpdateSide()) {
            case ARTIST -> proposalBuilder
                .artistConfirmation(true)
                .organisationConfirmation(null);
            case ORGANIZATION -> proposalBuilder
                .organisationConfirmation(true)
                .artistConfirmation(null);
        };

        return proposalBuilder.build();
    }
}

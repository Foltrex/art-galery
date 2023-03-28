package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.ProposalDto;
import com.scnsoft.art.entity.Proposal;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ArtistMapper.class,
        OrganizationMapper.class,
        FacilityMapper.class,
        CurrencyMapper.class
})
public abstract class ProposalMapper {

    public abstract ProposalDto mapToDto(Proposal proposal);

    public abstract Proposal mapToEntity(ProposalDto proposalDto);

    public Page<ProposalDto> mapPageToDto(Page<Proposal> proposalPage) {
        List<ProposalDto> proposalDtos = proposalPage
                .stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = proposalPage.getPageable();
        long totalElements = proposalPage.getTotalElements();
        return new PageImpl<>(proposalDtos, pageable, totalElements);
    }
}

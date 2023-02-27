package com.scnsoft.art.dto.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.entity.Artist;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class ArtistMapper {

    public abstract ArtistDto mapToDto(Artist artist);

    public abstract Artist mapToEntity(ArtistDto artistDto);

    public Page<ArtistDto> mapPageToDto(final Page<Artist> artistPage) {
        return new PageImpl<>(artistPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), artistPage.getPageable(), artistPage.getTotalElements());
    }
}
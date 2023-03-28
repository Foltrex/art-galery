package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.entity.Art;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public abstract class ArtMapper {

    public abstract ArtDto mapToDto(Art art);

    public abstract Art mapToEntity(ArtDto artDto);

    public Page<ArtDto> mapPageToDto(Page<Art> artPage) {
        List<ArtDto> artDtos = artPage
                .stream()
                .map(this::mapToDto)
                .toList();

        Pageable pageable = artPage.getPageable();
        long totalElements = artPage.getTotalElements();
        return new PageImpl<>(artDtos, pageable, totalElements);
    }
}

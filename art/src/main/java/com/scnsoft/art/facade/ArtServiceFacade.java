package com.scnsoft.art.facade;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {

    private final ArtService artService;
    private final ArtMapper artMapper;


    public Page<ArtDto> findAllByAccountIdAndName(UUID accountId, Pageable pageable, String searchText, String searchFilter, String searchOption) {
        Page<Art> artPage = artService.findAllByAccountIdAndName(
            accountId, pageable, searchText, searchFilter, searchOption
        );
        return artMapper.mapPageToDto(artPage);
    }

    public Page<ArtDto> findAllByArtistId(UUID artistId, Pageable pageable) {
        return artMapper.mapPageToDto(artService.findAllByArtistId(artistId, pageable));
    }

    public ArtDto findById(UUID id) {
        return artMapper.mapToDto(artService.findById(id));
    }

    public ArtDto save(ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        return artMapper.mapToDto(artService.save(art));
    }

    public void deleteById(UUID id) {
        artService.deleteById(id);
    }
}

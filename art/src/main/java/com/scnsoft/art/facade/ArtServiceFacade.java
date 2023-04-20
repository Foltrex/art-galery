package com.scnsoft.art.facade;

import java.util.UUID;

import com.scnsoft.art.dto.ArtFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {

    private final ArtService artService;
    private final ArtMapper artMapper;

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

    public void deleteByAccountId(UUID accountId) {
        artService.deleteByAccountId(accountId);
    }

    public Page<ArtDto> findAll(Pageable pageable, ArtFilter artFilter) {
        Page<Art> artPage = artService.findAll(pageable, artFilter);
        return artMapper.mapPageToDto(artPage);
    }
}

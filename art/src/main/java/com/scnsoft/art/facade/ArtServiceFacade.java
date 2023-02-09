package com.scnsoft.art.facade;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.impl.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {
    
    private final ArtService artService;
    private final ArtMapper artMapper;


    public Page<ArtDto> findAllByAccountId(UUID accountId, Pageable pageable) {
        return artMapper.mapPageToDto(artService.findAllByAccountId(accountId, pageable));
    }

    public ArtDto findById(UUID id) {
        return artMapper.mapToDto(artService.findById(id));
    }

    public ArtDto save(ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        return artMapper.mapToDto(artService.save(art));
    }
}

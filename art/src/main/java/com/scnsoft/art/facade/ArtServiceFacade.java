package com.scnsoft.art.facade;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.impl.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;
import com.scnsoft.art.feignclient.FileFeignClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {
    
    private final ArtService artService;
    private final ArtMapper artMapper;
    private final FileFeignClient fileFeignClient;


    public List<ArtDto> findAllByAccountId(UUID accountId) {
        return artService.findAllByAccountId(accountId)
            .stream()
            .map(artMapper::mapToDto)
            .toList();
    }

    public ArtDto findById(UUID id) {
        return artMapper.mapToDto(artService.findById(id));
    }

    public ArtDto save(ArtDto artDto) {
        ArtDto artDto = fileFeignClient.save(artDto);
        Art art = artMapper.mapToEntity(artDto);
        return artMapper.mapToDto(artService.save(art));
    }
}

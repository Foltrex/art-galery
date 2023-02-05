package com.scnsoft.art.facade;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.dto.mapper.impl.RepresentativeMapper;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.service.impl.RepresentativeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepresentativeServiceFacade {

    private final RepresentativeServiceImpl representativeService;
    private final RepresentativeMapper representativeMapper;


    public Page<RepresentativeDto> findAll(Pageable pageable) {
        return representativeMapper.mapPageToDto(representativeService.findAll(pageable));
    }

    public Page<RepresentativeDto> findAllByAccountId(UUID accountId, Pageable pageable) {
        return representativeMapper.mapPageToDto(representativeService.findAllByAccountId(accountId, pageable));
    }

    public RepresentativeDto findById(UUID id) {
        return representativeMapper.mapToDto(representativeService.findById(id));
    }

    public RepresentativeDto save(RepresentativeDto representativeDto) {
        Representative representative = representativeMapper.mapToEntity(representativeDto);
        return representativeMapper.mapToDto(representativeService.save(representative));
    }

    public RepresentativeDto updateById(UUID id, RepresentativeDto representativeDto) {
        Representative representative = representativeMapper.mapToEntity(representativeDto);
        return representativeMapper.mapToDto(representativeService.update(id, representative));
    }

    public Void deleteById(@PathVariable UUID id) {
        representativeService.deleteById(id);
        return null;
    }

    public Void deleteByAccountId(@PathVariable UUID accountId) {
        representativeService.deleteByAccountId(accountId);
        return null;
    }

}

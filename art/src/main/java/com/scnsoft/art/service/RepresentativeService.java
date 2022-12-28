package com.scnsoft.art.service;

import com.scnsoft.art.dto.RepresentativeDto;
import com.scnsoft.art.dto.mapper.impl.RepresentativeMapper;
import com.scnsoft.art.entity.Representative;
import com.scnsoft.art.exception.ArtResourceNotFoundException;
import com.scnsoft.art.repository.RepresentativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record RepresentativeService(RepresentativeRepository representativeRepository,
                                    RepresentativeMapper representativeMapper) {

    public List<RepresentativeDto> findAll() {
        return representativeRepository.findAll().stream()
                .map(representativeMapper::mapToDto)
                .toList();
    }

    public RepresentativeDto findById(UUID id) {
        return representativeRepository
                .findById(id)
                .map(representativeMapper::mapToDto)
                .orElseThrow(ArtResourceNotFoundException::new);
    }

    public RepresentativeDto save(RepresentativeDto RepresentativeDto) {
        Representative representative = representativeMapper.mapToEntity(RepresentativeDto);
        return representativeMapper.mapToDto(representativeRepository.save(representative));
    }

    public RepresentativeDto update(UUID id, RepresentativeDto RepresentativeDto) {
        Representative representative = representativeMapper.mapToEntity(RepresentativeDto);
        representative.setId(id);
        return representativeMapper.mapToDto(representativeRepository.save(representative));
    }

    public void deleteById(UUID id) {
        representativeRepository.deleteById(id);
    }
}

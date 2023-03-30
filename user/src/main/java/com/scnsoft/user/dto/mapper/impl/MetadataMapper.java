package com.scnsoft.user.dto.mapper.impl;

import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.dto.mapper.Mapper;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MetadataMapper implements Mapper<Metadata, MetadataDto> {
    @Override
    public MetadataDto mapToDto(Metadata metadata) {
        return MetadataDto.builder()
                .key(metadata.getMetadataId().getKey())
                .value(metadata.getValue())
                .build();
    }

    //@TODO maybe do later, but why?
    @Override
    public Metadata mapToEntity(MetadataDto metadataDto) {
        return Metadata.builder()
                .value(metadataDto.getValue())
                .build();
    }

    public List<MetadataDto> mapToDtoList(List<Metadata> metadata) {
        return metadata.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<Metadata> mapToList(List<MetadataDto> metadataDtosList, UUID accountId) {
        return metadataDtosList.stream().map(metadataDto -> Metadata.builder()
                .metadataId(MetadataId.builder()
                        .accountId(accountId)
                        .key(metadataDto.getKey())
                        .build())
                .value(metadataDto.getValue())
                .build()).collect(Collectors.toList());
    }

}

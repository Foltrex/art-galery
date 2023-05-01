package com.scnsoft.user.dto.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scnsoft.user.dto.MetadataDto;
import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;

@Mapper(componentModel = "spring")
public abstract class MetadataMapper {
    @Mapping(target = "value", source = "metadataDto.value")
    @Mapping(target = "metadataId.key", source = "metadataDto.key")
    public abstract Metadata mapToEntity(MetadataDto metadataDto);

    @Mapping(target = "key", source = "metadata.metadataId.key")
    @Mapping(target = "value", source = "value")
    public abstract MetadataDto mapToDto(Metadata metadata);

    public Set<Metadata> mapToList(Set<MetadataDto> metadata, UUID accountId) {
        return metadata
            .stream()
            .map(metadataDto -> Metadata.builder()
                .metadataId(
                    MetadataId.builder()
                        .accountId(accountId)
                        .key(metadataDto.getKey())
                        .build()
                )
                .value(metadataDto.getValue())
                .build())
            .collect(Collectors.toSet());
    }
}

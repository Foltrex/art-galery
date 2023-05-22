package com.scnsoft.art.dto.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.scnsoft.art.dto.MetaDataDto;
import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.MetadataId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MetadataMapper {

    @Mapping(target = "value", source = "metadataDto.value")
    @Mapping(target = "metadataId.key", source = "metadataDto.key")
    public abstract Metadata mapToEntity(MetaDataDto metadataDto);

    @Mapping(target = "key", source = "metadata.metadataId.key")
    @Mapping(target = "value", source = "value")
    public abstract MetaDataDto mapToDto(Metadata metadata);

    public Set<Metadata> mapToList(Set<MetaDataDto> metadata, UUID accountId) {
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

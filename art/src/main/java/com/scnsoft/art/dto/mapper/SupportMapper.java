package com.scnsoft.art.dto.mapper;

import com.scnsoft.art.dto.SupportDto;
import com.scnsoft.art.dto.SupportThreadDto;
import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SupportMapper {

    public abstract SupportDto mapToDto(Support support);

    public abstract Support mapToEntity(SupportDto support);

    public abstract SupportThreadDto mapToThreadDto(SupportThread support);

    public abstract SupportThread mapToThreadEntity(SupportThreadDto support);
}
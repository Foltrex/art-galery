package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityFileInfoDto {
    private UUID id;
    private String mimeType;
    private Integer contentLength;
    private Boolean isPrimary;
}

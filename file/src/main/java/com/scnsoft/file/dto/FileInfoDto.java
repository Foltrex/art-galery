package com.scnsoft.file.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileInfoDto {
    private UUID id;
    private String mimeType;
    private Integer contentLength;
}

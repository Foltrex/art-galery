package com.scnsoft.file.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileInfoDto {
    private UUID id;
    private UUID artId;
    private String mimeType;
    private String filename;
}

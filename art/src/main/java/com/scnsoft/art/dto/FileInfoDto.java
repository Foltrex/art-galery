package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileInfoDto {
    private UUID id;
    private String data;
    private String mimeType;
    private String filename;
}

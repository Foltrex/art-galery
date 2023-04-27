package com.scnsoft.file.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UploadFileDto {
    private String data;
    private String mimeType;
}

package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UploadFileDto {
    private UUID artId;
    private String data;
    private String mimeType;
    private String filename;
}

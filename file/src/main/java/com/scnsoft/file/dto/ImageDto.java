package com.scnsoft.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {
    private Long artId;
    private String imageData;
    private String mimeType;
    private String filename;
}

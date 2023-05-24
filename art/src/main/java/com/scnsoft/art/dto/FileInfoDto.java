package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {
    private UUID id;
    private String mimeType;
    private Integer contentLength;
    private String directory;
    private String originalName;
    private Date createdAt;
    private Integer cacheControl;

    private String data;
}

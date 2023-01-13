package com.scnsoft.file.dto;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

@Data
@Builder
public class FileStreamDto {
    private InputStream inputStream;
}

package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetadataDto {
    private String key;
    private String value;
}

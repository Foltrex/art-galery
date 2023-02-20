package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CurrencyDto {
    private UUID id;
    private String value;
    private String label;
}

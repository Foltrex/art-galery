package com.scnsoft.art.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CityDto {
    UUID id;
    String name;
    Double latitude;
    Double longitude;
}

package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CityDto {
    UUID id;
    String name;
    Double latitude;
    Double longitude;
}

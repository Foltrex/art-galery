package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CityDto {
    UUID id;
    String name;
    Double latitude;
    Double longitude;
}

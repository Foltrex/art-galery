package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AddressDto {
    UUID id;
    CityDto cityDto;
    String streetName;
    Integer streetNumber;
}

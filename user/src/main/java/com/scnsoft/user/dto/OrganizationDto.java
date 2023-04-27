package com.scnsoft.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {
    public enum Status {
        NEW,
        ACTIVE,
        INACTIVE
    }

    private UUID id;
    private String name;
    private AddressDto address;
    private Status status;
}
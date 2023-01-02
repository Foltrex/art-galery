package com.scnsoft.user.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class RegisterRepresentativeRequestDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    private UUID organizationId;

    private UUID facilityId;
}

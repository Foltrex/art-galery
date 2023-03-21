package com.scnsoft.user.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRepresentativeRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotNull
    private UUID organizationId;

    private UUID facilityId;
}

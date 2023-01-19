package com.scnsoft.user.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class LoginRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}

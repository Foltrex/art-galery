package com.scnsoft.user.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PasswordRecoveryRequest {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer code;

    @NotBlank
    private String password;
}

package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryRequest {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer code;

    @NotBlank
    private String password;
}

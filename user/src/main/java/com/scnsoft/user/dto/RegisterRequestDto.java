package com.scnsoft.user.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class RegisterRequestDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String accountType;
}

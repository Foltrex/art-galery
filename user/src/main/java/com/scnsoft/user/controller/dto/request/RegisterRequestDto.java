package com.scnsoft.user.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDto {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    private String accountType;
}

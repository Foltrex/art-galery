package com.scnsoft.user.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordRequest {
    @NotBlank(message = "Old password can't be empty")
    @Size(min = 6, max = 255)
    private String oldPassword;

    @NotBlank(message = "New password can't be empty")
    @Size(min = 6, max = 255)
    private String newPassword;
}

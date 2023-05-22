package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank(message = "Old password can't be empty")
    @Size(min = 6, max = 255)
    private String oldPassword;

    @NotBlank(message = "New password can't be empty")
    @Size(min = 6, max = 255)
    private String newPassword;
}

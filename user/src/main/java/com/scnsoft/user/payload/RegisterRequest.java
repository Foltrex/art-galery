package com.scnsoft.user.payload;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.scnsoft.user.dto.MetadataDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String accountType;

    @NotEmpty
    private String firstname;

    private String lastname;

    // @NotEmpty
    @Builder.Default
    private List<MetadataDto> metadata = new ArrayList<>();
}

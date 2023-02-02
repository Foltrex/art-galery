package com.scnsoft.user.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class DeleteAccountRequest {

    @NotNull
    private UUID accountId;

    @NotEmpty
    private String accountPassword;
}

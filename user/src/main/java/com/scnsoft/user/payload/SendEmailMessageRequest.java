package com.scnsoft.user.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class SendEmailMessageRequest {

    @Email
    @NotEmpty
    private String receiver;

}

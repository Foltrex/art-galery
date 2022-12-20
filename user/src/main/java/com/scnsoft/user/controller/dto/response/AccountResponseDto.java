package com.scnsoft.user.controller.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountResponseDto {

    private UUID id;
    private String login;
    private String token;
}

package com.scnsoft.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountResponseDto {

    private UUID id;
    private String login;
    private String token;
}

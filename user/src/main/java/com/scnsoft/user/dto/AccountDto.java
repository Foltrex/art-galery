package com.scnsoft.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Date blockedSince;
    private Boolean isApproved;
    private String accountType;

    @Builder.Default
    private List<MetadataDto> metadata = new ArrayList<>();
}

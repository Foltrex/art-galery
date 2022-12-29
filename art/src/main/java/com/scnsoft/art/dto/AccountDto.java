package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class AccountDto {
    private UUID id;
    private String email;
    private Date lastFail;
    private Integer failCount;
    private Date blockedSince;
    private Boolean isApproved;
    private String accountType;
}

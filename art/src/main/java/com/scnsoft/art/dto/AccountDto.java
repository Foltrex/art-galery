package com.scnsoft.art.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

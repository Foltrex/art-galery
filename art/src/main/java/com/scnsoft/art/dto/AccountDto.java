package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Date lastFail;
    private Integer failCount;
    private String password;
    private Date blockedSince;
    private Long blockDuration;
    private Account.BlockReason blockReason;
    private Boolean isApproved;
    private AccountType accountType;
    private Set<MetaDataDto> metadata;
}

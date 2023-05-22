package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
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
    private Boolean isApproved;
    private AccountType accountType;
    private Set<MetaDataDto> metadata;
}

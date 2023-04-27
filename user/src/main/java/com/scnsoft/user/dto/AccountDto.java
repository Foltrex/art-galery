package com.scnsoft.user.dto;

import com.scnsoft.user.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private UUID id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private Account.AccountType accountType;

    private String password;
    private Date blockedSince;
    private Boolean isApproved;

    @Builder.Default
    private List<MetadataDto> metadata = new ArrayList<>();
}

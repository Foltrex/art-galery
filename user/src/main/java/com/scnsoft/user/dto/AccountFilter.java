package com.scnsoft.user.dto;

import com.scnsoft.user.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountFilter {

    private String username;
    private Account.AccountType usertype;
    private UUID organizationId;
    private UUID cityId;
}

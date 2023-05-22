package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountFilter {

    private String name;
    private AccountType usertype;
    private UUID organizationId;
    private UUID cityId;
}

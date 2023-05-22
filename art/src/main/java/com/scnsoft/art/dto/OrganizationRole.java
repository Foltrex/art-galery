package com.scnsoft.art.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrganizationRole {
    CREATOR(10),
    MODERATOR(5),
    MEMBER(1),
    UNKNOWN(-1);

    private final int strength;

    public static OrganizationRole fromString(String str) {
        for(OrganizationRole role : OrganizationRole.values()) {
            if(role.name().equals(str)) {
                return role;
            }
        }
        return UNKNOWN;
    }
}

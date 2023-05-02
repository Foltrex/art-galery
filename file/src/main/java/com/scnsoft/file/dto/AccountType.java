package com.scnsoft.file.dto;

import com.google.common.base.Strings;

public enum AccountType {
    REPRESENTATIVE, ARTIST, SYSTEM;

    public static AccountType fromString(String accountType) {
        if (Strings.isNullOrEmpty(accountType)) {
            return null;
        }
        return AccountType.valueOf(accountType.toUpperCase());
    }
}
package com.scnsoft.art.dto;

public enum AccountType {
    REPRESENTATIVE, ARTIST, SYSTEM;

    public static AccountType fromString(String accountType) {
        if (accountType == null) {
            return null;
        }
        return AccountType.valueOf(accountType.toUpperCase());
    }
}

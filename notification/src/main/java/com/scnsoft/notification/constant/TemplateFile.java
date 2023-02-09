package com.scnsoft.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TemplateFile {
    REPRESENTATIVE_REGISTRATION("representative_registration.ftl"),
    PASSWORD_RECOVERY("password_recovery.ftl");

    private final String name;
}

package com.scnsoft.art.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TemplateFile {
    USER_REGISTRATION("user_registration.ftl"),
    PASSWORD_RECOVERY("password_recovery.ftl");

    private final String name;
}


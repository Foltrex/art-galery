package com.scnsoft.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class WrongAccessPermissionException extends RuntimeException {
    public WrongAccessPermissionException() {
        super();
    }
    public WrongAccessPermissionException(String message) {
        super(message);
    }
}

package com.scnsoft.art.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class ArtResourceNotFoundException extends RuntimeException {

    public ArtResourceNotFoundException() {
    }

    public ArtResourceNotFoundException(String message) {
        super(message);
    }

    public ArtResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ArtResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

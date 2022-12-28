package com.scnsoft.art.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ArtResourceNotFoundException.class)
    public ResponseEntity<Object> handleException(
        ArtResourceNotFoundException ex, 
        WebRequest req
    ) {
        return handleExceptionInternal(ex, ex.getMessage(), 
            new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

}

package com.scnsoft.user.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignResponseException extends RuntimeException {

    public FeignResponseException(FeignException e) {
        int status = e.status();
        throw new ResponseStatusException(HttpStatus.valueOf(status == -1 ? 502 : status), e.getMessage());
    }

}

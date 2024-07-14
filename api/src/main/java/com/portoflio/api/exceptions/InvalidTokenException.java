package com.portoflio.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(){}
    public InvalidTokenException(String message){
        super(message);
    }
    public InvalidTokenException(String message, Throwable cause){
        super(message, cause);
    }
    public InvalidTokenException(Throwable cause){
        super(cause);
    }
}

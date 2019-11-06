package com.example.teamE.endpoints.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNotValidException extends RuntimeException {
    public UserNotValidException(String message) {
        super(message);
    }
}

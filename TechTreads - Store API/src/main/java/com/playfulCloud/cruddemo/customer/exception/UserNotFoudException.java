package com.playfulCloud.cruddemo.customer.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoudException extends RuntimeException{
    public UserNotFoudException(String s) {
        super(s);
    }

}

package com.arpanm.orderservice.web;

import com.arpanm.orderservice.exception.InvalidOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerAdvice {

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onInvalidOrderException(InvalidOrderException ex) {
        return ex.getMessage();
    }
}

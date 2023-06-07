package com.arpanm.orderservice.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(Throwable cause) {
        super(cause);
    }
}

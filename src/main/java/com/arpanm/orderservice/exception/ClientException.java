package com.arpanm.orderservice.exception;

public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}

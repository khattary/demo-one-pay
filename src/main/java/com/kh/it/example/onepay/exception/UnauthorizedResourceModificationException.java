package com.kh.it.example.onepay.exception;

public class UnauthorizedResourceModificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedResourceModificationException(String message) {
        super(message);
    }

    public UnauthorizedResourceModificationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

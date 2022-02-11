package com.cshrob.szakdolgozat.exception;

public class NotConfiguredException extends Exception {
    private final String message;

    public NotConfiguredException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

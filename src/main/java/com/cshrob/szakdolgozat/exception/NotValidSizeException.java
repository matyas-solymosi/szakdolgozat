package com.cshrob.szakdolgozat.exception;

public class NotValidSizeException extends Throwable {
    private final String message;

    public NotValidSizeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
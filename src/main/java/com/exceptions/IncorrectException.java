package com.exceptions;

public class IncorrectException extends RuntimeException {

    public IncorrectException() {
        super("Incorrect file");
    }

    public IncorrectException(String message) {
        super("Incorrect file: " + message);
    }
}

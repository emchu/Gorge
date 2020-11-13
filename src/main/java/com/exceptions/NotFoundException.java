package com.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Could not find");
    }

    public NotFoundException(String message) {
        super("Could not find: " + message);
    }
}

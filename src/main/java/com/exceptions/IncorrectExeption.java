package com.exceptions;

public class IncorrectExeption extends RuntimeException {

    public IncorrectExeption() {
        super("Incorrect file");
    }

    public IncorrectExeption(String message) {
        super("Incorrect file: " + message);
    }
}

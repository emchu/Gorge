package com.exceptions;

public class SiteNotFound extends RuntimeException {

    public SiteNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
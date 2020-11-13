package com.security.payload;

import lombok.Getter;
import lombok.Setter;

public class ApiResponse {

    @Setter @Getter
    private Boolean success;

    @Setter @Getter
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

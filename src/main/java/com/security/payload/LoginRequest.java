package com.security.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    @Setter @Getter
    private String email;

    @NotBlank
    @Setter @Getter
    private String password;
}
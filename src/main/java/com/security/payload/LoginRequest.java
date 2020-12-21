package com.security.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@JsonPropertyOrder({ "email", "password" })
public class LoginRequest  {
    @NotBlank
    @Setter @Getter
    private String email;

    @NotBlank
    @Setter @Getter
    private String password;
}
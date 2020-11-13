package com.users.model.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@JsonPropertyOrder({ "email", "password" })
public class Login {

    @Getter
    @Setter
    @Email
    private String email;

    @Getter
    @Setter
    private String password;

    @JsonCreator
    public Login(@JsonProperty("email") String email, @JsonProperty("password") String password){
        this.password = password;
        this.email = email;
    }
}

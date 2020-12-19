package com.users.model.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@JsonPropertyOrder({ "email", "password" })
public class RegisterUser {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

}

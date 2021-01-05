package com.users.model.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@JsonPropertyOrder({ "email", "password", "confirmPassword" })
public class RegisterUser {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String confirmPassword;

    public boolean checkPassword() {
        return passwordHasRequiredChar()
                && passwordSize()
                && passwordCharSequence();
    }
    public boolean samePassword() {
        return password.equals(confirmPassword);
    }

    private boolean passwordSize() {
        return password.length() > 7;
    }

    private boolean passwordHasRequiredChar(){
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(password);
        boolean result = match.find();

        return result && password.matches(".*\\d.*");
    }

    private boolean passwordCharSequence(){
        Pattern pattern = Pattern.compile("^(([a-z0-9!@#$%^&*()_+|~\\- =`{}\\[\\]:\"<>?,./A-Z])\\2?(?!\\2))+$");
        Matcher match = pattern.matcher(password);

        return match.find();
    }
}

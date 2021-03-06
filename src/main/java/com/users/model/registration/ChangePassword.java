package com.users.model.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@JsonPropertyOrder({"password", "newPassword", "newPassword2" })
public class ChangePassword {

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String newPassword;

    @Getter @Setter
    private String newPassword2;

    @JsonCreator
    public ChangePassword(@JsonProperty("password") String password,
                          @JsonProperty("newPassword") String newPassword,
                          @JsonProperty("newPassword2") String newPassword2) {

        this.password = password;
        this.newPassword = newPassword;
        this.newPassword2 = newPassword2;
    }

    public ChangePassword(){}

    public boolean checkNewPassword() {
        String passwordToChange = getNewPassword();
        return passwordHasRequiredChar(passwordToChange)
                && passwordSize(passwordToChange)
                && passwordCharSequence(passwordToChange);
    }

    public boolean samePassword(){
        return newPassword.equals(newPassword2);
    }

    private boolean passwordSize(String newPassword) {
        return newPassword.length() > 7;
    }

    private boolean passwordHasRequiredChar(String newPassword){
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(newPassword);
        boolean result = match.find();

        return result && newPassword.matches(".*\\d.*");
    }

    private boolean passwordCharSequence(String newPassword){
        Pattern pattern = Pattern.compile("^(([a-z0-9!@#$%^&*()_+|~\\- =`{}\\[\\]:\"<>?,./A-Z])\\2?(?!\\2))+$");
        Matcher match = pattern.matcher(newPassword);

        return match.find();
    }
}

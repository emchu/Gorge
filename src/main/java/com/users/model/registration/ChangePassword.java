package com.users.model.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonPropertyOrder({"password","newPassword", "newPassword2" })
public class ChangePassword {

//    @Getter @Setter
//    private String email;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String newPassword;

    @Getter @Setter
    private String newPassword2;

    @JsonCreator
    public ChangePassword(
//            @JsonProperty("email") String email,
                          @JsonProperty("password") String password,
                          @JsonProperty("newPassword") String newPassword,
                          @JsonProperty("newPassword2") String newPassword2) {

        this.password = password;
//        this.email = email;
        this.newPassword = newPassword;
        this.newPassword2 = newPassword2;
    }
    public ChangePassword(){}

    public boolean checkNewPassword() {
        String passwordToChange = getNewPassword();
        String passwordToChange2 = getNewPassword2();
        return passwordHasRequiredChar(passwordToChange)
                && passwordSize(passwordToChange)
                && passwordCharSequence(passwordToChange)
                && samePassword(passwordToChange, passwordToChange2);
    }

    private boolean samePassword(String newPassword, String newPassword2){
        return newPassword.equals(newPassword2);
    }

    private boolean passwordSize(String newPassword) {
        return newPassword.length() > 10;
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

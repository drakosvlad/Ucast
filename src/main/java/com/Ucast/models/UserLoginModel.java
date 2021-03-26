package com.Ucast.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserLoginModel {

    @NotEmpty
    @NotNull
    @Email
    private String email;

    @NotEmpty
    @NotNull
    private String password;

    public UserLoginModel(String email, String password){
        this.email = email;
        this.password = password;
    }

    public UserLoginModel(){

    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

package com.Ucast.models;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegistrationModel {

    @NotNull
    @NotEmpty
    private String username;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;
    private String avatarUrl;

    public UserRegistrationModel() {}


    public UserRegistrationModel(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserRegistrationModel(String username, String email, String password, String avatarUrl){
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}

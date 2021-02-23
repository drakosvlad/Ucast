package com.Ucast.models;

public class AuthorModel {

    private String name;
    private String email;

    public AuthorModel(String name, String email){
        this.name = name;
        this.email = email;
    }

    public AuthorModel(MongoAuthorModel that){
        this.email = that.getEmail();
        this.name = that.getName();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}

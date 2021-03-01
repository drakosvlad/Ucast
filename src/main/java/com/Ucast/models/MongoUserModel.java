package com.Ucast.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class MongoUserModel {

    @Id
    private String id;
    private String username;
    private String email;
    private String avatarUrl;   //??

//    @JsonIgnore
    private String password;


    public MongoUserModel(){}

    public MongoUserModel(String username, String email, String password, String avatarUrl){
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "MongoUserModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}

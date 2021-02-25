package com.Ucast.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class MongoUserModel {

    @Id
    private String id;
    private String username;
    private String email;
    private String avatarUrl;   //??

    public MongoUserModel(){}

    public MongoUserModel(String username, String email){
        this.username = username;
        this.email = email;
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
}

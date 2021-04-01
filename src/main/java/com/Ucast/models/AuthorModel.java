package com.Ucast.models;

import org.bson.types.ObjectId;

public class AuthorModel {

    private String id;
    private String name;
    private ObjectId userId;


    public AuthorModel(String name, ObjectId userId){
        this.name = name;
        this.userId = userId;
    }

    public AuthorModel(){}

    public AuthorModel(MongoAuthorModel that){
        this.name = that.getName();
        this.userId = that.getUserId();
        this.id = that.getId();
    }

    public String getName() {
        return name;
    }

    public ObjectId getUserId() {
        return userId;
    }


//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}

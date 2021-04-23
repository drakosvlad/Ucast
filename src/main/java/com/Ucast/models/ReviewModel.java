package com.Ucast.models;

import org.bson.types.ObjectId;

public class ReviewModel {

//    private String id;
    private String text;
    private int rate;
    private String userId;
    private String userName;
//    private ObjectId podcastId;

    public ReviewModel(String text, int rate){
        this.text = text;
        this.rate = rate;
    }

    public ReviewModel() {}

    public int getRate() {
        return rate;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}

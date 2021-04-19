package com.Ucast.models;

import org.bson.types.ObjectId;

public class AuthorModel {

//    private String id;
//    private String name;
//    private ObjectId userId;
    private String channelName;
    private String coverURL;


    public AuthorModel(String channelName, String coverURL){
        this.channelName = channelName;
        this.coverURL = coverURL;
    }

    public AuthorModel(){}
//
//    public AuthorModel(MongoAuthorModel that){
//        this.name = that.getName();
//        this.userId = that.getUserId();
//        this.id = that.getId();
//    }

//    public String getName() {
//        return name;
//    }
//
//    public ObjectId getUserId() {
//        return userId;
//    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCoverURL() {
        return coverURL;
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

package com.Ucast.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "Authors")
public class MongoAuthorModel {

    @Id
    private String id;
    private String name;
    private String description;
    private ObjectId userId;
    private List<ObjectId> podcastIdList;
    private String coverURL;
    private String channelName;
    private boolean confirmed;

    public MongoAuthorModel() {}

    public MongoAuthorModel(MongoUserModel user){
        this.name = user.getUsername();
        this.userId = user.getObjectId();
        this.description = "";
        this.confirmed = false;
        this.podcastIdList = new ArrayList<>();
    }

    public MongoAuthorModel(String name, ObjectId userId){
        this.name = name;
        this.userId = userId;
    }

//    public MongoAuthorModel(AuthorModel that){
//        this.name = that.getName();
//        this.userId = that.getUserId();
//    }

    @Override
    public String toString() {
        return String.format(
                "Author [id=%s, name=%s, email=%s]",
                id, name, userId);
    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    public ObjectId getUserId() {
        return userId;
    }

    public ObjectId getObjectId() {
        return new ObjectId(this.id);
    }

    public String getDescription(){
        return this.description;
    }

    public List<ObjectId> getPodcastIdList() {
        return podcastIdList;
    }

    public void addPodcast(ObjectId podcastId){
        this.podcastIdList.add(podcastId);
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

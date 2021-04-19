package com.Ucast.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Users")
public class MongoUserModel {

    @Id
    private String id;
    private String username;
    private String email;
    private String avatarUrl;   //??
    private String password;
    private List<ObjectId> favoritePodcasts;


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

    public ObjectId getObjectId(){
        return new ObjectId(this.id);
    }

    public String getPassword() {
        return password;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFavoritePodcasts(List<ObjectId> favoritePodcasts) {
        this.favoritePodcasts = favoritePodcasts;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFavoritePoscast(ObjectId podcastId){
        this.favoritePodcasts.add(podcastId);
    }

    public void removeFavoritePodcast(ObjectId podcastId){
        this.favoritePodcasts.remove(podcastId);
    }

    public List<ObjectId> getFavoritePodcasts() {
        return favoritePodcasts;
    }
}

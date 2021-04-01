package com.Ucast.models;

import org.bson.types.ObjectId;

public class PodcastModel {

    private String id;
    private String name;
    private String authorName;
    private String filepath;
    private String description;

    public PodcastModel(MongoPodcastModel that){
        id = that.getId();
        name = that.getName();
        authorName = that.getAuthorName();
        filepath = that.getFilepath();
        description = that.getDescription();
    }

    public PodcastModel(){}

    public PodcastModel(String name, String filepath, String description){
        this.name = name;
        this.description = description;
        this.filepath = filepath;
    }

    public PodcastModel(String id, String name, String filepath, String description){
        this.id = id;  // id from podcast link
        this.name = name;
        this.description = description;
        this.filepath = filepath;
    }


    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}

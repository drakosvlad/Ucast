package com.Ucast.models;

import org.bson.types.ObjectId;

public class PodcastModel {

    private String id;
    private String name;
    private ObjectId authorId;
    private String authorName;
    private String filepath;
    private boolean isChecked;
    private String description;

    public PodcastModel(MongoPodcastModel that){
        id = that.getId();
        authorId = that.getAuthorId();
        name = that.getName();
        authorName = that.getAuthorName();
        filepath = that.getFilepath();
        description = that.getDescription();
    }

    public String getName() {
        return name;
    }

    public ObjectId getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getFilepath() {
        return filepath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getDescription() {
        return description;
    }
}

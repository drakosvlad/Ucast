package com.Ucast.models;

import org.bson.types.ObjectId;

public class PodcastModel {

    private String id;
    private String name;
    private String authorName;
    private String filepath;
    private String description;
    private String photoURL;
    private String timecodes;

    public int getListened() {
        return listened;
    }

    private int listened;

    public PodcastModel(MongoPodcastModel that){
        id = that.getId();
        name = that.getName();
        authorName = that.getAuthorName();
        filepath = that.getFilepath();
        description = that.getDescription();
        photoURL = that.getPhotoURL();
        timecodes = that.getTimecodes();
        listened = that.getListened();
    }

    public PodcastModel(){}

    public PodcastModel(String name, String filepath, String description, String photoURL, String timecodes, int listened){
        this.name = name;
        this.description = description;
        this.filepath = filepath;
        this.photoURL = photoURL;
        this.timecodes = timecodes;
        this.listened = listened;
    }

    public PodcastModel(String id, String name, String filepath, String description, String photoURL, String timecodes, int listened){
        this.id = id;  // id from podcast link
        this.name = name;
        this.description = description;
        this.filepath = filepath;
        this.photoURL = photoURL;
        this.timecodes = timecodes;
        this.listened = listened;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public String getTimecodes() { return timecodes; }
}

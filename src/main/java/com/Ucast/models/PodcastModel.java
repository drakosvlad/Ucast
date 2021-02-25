package com.Ucast.models;

import org.bson.types.ObjectId;

public class PodcastModel {

    private String name;
    private ObjectId authorId;
    private String authorName;
    private String filepath;
    private boolean isChecked;
    private String description;

}

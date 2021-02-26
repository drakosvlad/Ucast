package com.Ucast.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Podcasts")
public class MongoPodcastModel {

    @Id
    private String id;

    private String name;
    private ObjectId authorId;
    private String authorName;
    private String filepath;
    private boolean isChecked;
    private String description;


    public MongoPodcastModel(){}

    public MongoPodcastModel(String name, ObjectId authorId, String authorName, String filepath){
        this.authorId = authorId;
        this.name = name;
        this.authorName = authorName;
        this.filepath = filepath;
    }

    public MongoPodcastModel(String name, ObjectId authorId, String authorName, String filepath, String description){
        this.authorId = authorId;
        this.name = name;
        this.authorName = authorName;
        this.filepath = filepath;
        this.description = description;
    }

    public MongoPodcastModel(PodcastModel that){
        authorId = that.getAuthorId();
        name = that.getName();
        authorName = that.getAuthorName();
        filepath = that.getFilepath();
        description = that.getDescription();
    }

    @Override
    public String toString() {
        return "MongoPodcastModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", filepath='" + filepath + '\'' +
                ", isChecked=" + isChecked +
                ", description='" + description + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public ObjectId getAuthorId() {
        return authorId;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getFilepath() {
        return filepath;
    }
}

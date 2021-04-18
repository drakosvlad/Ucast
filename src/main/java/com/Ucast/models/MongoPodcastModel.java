package com.Ucast.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Podcasts")
public class MongoPodcastModel {

    @Id
    private String id;

    private String name;
    private ObjectId authorId;
    private String authorName;
    private String filepath;
    @JsonIgnore
    private boolean isChecked;
    private String description;
    private String photoURL;
    private int listened;
    private double rating;
//    @JsonIgnore
    private List reviews;

    public MongoPodcastModel(){}

//    public MongoPodcastModel(String name, ObjectId authorId, String authorName, String filepath){
//        this.authorId = authorId;
//        this.name = name;
//        this.authorName = authorName;
//        this.filepath = filepath;
//        this.isChecked = false;
//        this.description = "";
////        this.reviews = new ArrayList();
//    }
//
//    public MongoPodcastModel(String name, ObjectId authorId, String authorName, String filepath, String description){
//        this.authorId = authorId;
//        this.name = name;
//        this.authorName = authorName;
//        this.filepath = filepath;
//        this.description = description;
//        this.isChecked = false;
////        this.reviews = new ArrayList();
//    }

    public MongoPodcastModel(PodcastModel that){
        name = that.getName();
        authorName = that.getAuthorName();
        filepath = that.getFilepath();
        description = that.getDescription();
        photoURL = that.getPhotoURL();
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

    public String getId()  {return id;}

    public ObjectId getObjectId() {
        return new ObjectId(this.id);
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setAuthorId(ObjectId authorId) {
        this.authorId = authorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setListened(int listened) {
        this.listened = listened;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void addListened(){
        this.listened += 1;
    }

    public List getReviews() {
        return reviews;
    }

    public void addReview(ReviewModel review){
        this.reviews.add(review);
        int n = this.reviews.size();
        double diff = ((double)review.getRate() - this.rating)/n;
        this.rating += diff;
    }
}

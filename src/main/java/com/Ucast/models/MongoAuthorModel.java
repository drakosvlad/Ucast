package com.Ucast.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Authors")
public class MongoAuthorModel {

    @Id
    private String id;
    private String name;
    private ObjectId userId;

    public MongoAuthorModel() {}

    public MongoAuthorModel(String name, ObjectId userId){
        this.name = name;
        this.userId = userId;
    }

    public MongoAuthorModel(AuthorModel that){
        this.name = that.getName();
        this.userId = that.getUserId();
    }

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
}

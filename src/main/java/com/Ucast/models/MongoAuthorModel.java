package com.Ucast.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Authors")
public class MongoAuthorModel {

    @Id
    private String id;
    private String name;
    private String email;

    public MongoAuthorModel() {}

    public MongoAuthorModel(String name, String email){
        this.name = name;
        this.email = email;
    }

    public MongoAuthorModel(AuthorModel that){
        this.name = that.getName();
        this.email = that.getEmail();
    }

    @Override
    public String toString() {
        return String.format(
                "Author [id=%s, name=%s, email=%s]",
                id, name, email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() { return id; }
}

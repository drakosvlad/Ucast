package com.Ucast.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Authors")
public class AuthorModel {

    @Id
    private String id;

    private String name;
    private String email;

    public AuthorModel() {}

    public AuthorModel(String name, String email){
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "Author [id=%s, name=%s, email=%s]",
                id, name, email);
    }
}

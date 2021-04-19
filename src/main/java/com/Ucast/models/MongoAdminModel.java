package com.Ucast.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Admins")

public class MongoAdminModel {

    @Id
    private String id;
    private ObjectId userId;
}

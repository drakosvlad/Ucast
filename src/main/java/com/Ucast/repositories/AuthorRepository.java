package com.Ucast.repositories;

import com.Ucast.models.MongoAuthorModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<MongoAuthorModel, String> {
    public MongoAuthorModel findByName(String name);
    public MongoAuthorModel findById(ObjectId id);
    public MongoAuthorModel findByUserId(ObjectId userId);
    public MongoAuthorModel findByChannelName(String channelName);
}

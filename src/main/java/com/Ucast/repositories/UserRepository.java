package com.Ucast.repositories;

import com.Ucast.models.MongoUserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<MongoUserModel, String> {

    public MongoUserModel findByEmail(String Email);
    public MongoUserModel findById(ObjectId id);
//    public MongoUserModel findById()
}

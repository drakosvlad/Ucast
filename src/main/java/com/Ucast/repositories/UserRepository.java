package com.Ucast.repositories;

import com.Ucast.models.MongoUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<MongoUserModel, String> {

    public MongoUserModel findByEmail(String Email);
    public Optional<MongoUserModel> findById(String id);
//    public MongoUserModel findById()
}

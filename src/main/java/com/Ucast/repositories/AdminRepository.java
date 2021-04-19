package com.Ucast.repositories;

import com.Ucast.models.MongoAdminModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<MongoAdminModel, String> {

    public Optional<MongoAdminModel> findById(String id);
    public MongoAdminModel findByUserId(ObjectId id);
}

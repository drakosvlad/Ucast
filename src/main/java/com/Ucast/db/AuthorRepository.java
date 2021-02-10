package com.Ucast.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<MongoAuthorModel, String> {

    public MongoAuthorModel findByEmail(String email);
    public MongoAuthorModel findByName(String name);
}

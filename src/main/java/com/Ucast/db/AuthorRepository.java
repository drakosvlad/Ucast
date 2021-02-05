package com.Ucast.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<AuthorModel, String> {

    public AuthorModel findAuthorModelByEmail(String email);
    public AuthorModel findByName(String name);
}

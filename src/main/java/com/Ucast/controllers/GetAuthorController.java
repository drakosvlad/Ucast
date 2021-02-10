package com.Ucast.controllers;

import com.Ucast.db.AuthorRepository;
import com.Ucast.db.MongoAuthorModel;
import com.Ucast.models.AuthorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAuthorController {

    private AuthorRepository authorRepository;

    @RequestMapping("/author/{email}")
    public AuthorModel getAuthor(@PathVariable("email") String email){
        MongoAuthorModel mongoModel = authorRepository.findByEmail(email);
        return new AuthorModel(mongoModel.getName(), mongoModel.getEmail());
    }
}

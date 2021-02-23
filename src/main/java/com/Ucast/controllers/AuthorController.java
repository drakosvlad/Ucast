package com.Ucast.controllers;

import com.Ucast.repositories.AuthorRepository;
import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.AuthorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @RequestMapping("/author/{email}")
    public ResponseEntity<AuthorModel> getAuthor(@PathVariable("email") String email){
        MongoAuthorModel mongoModel = authorRepository.findByEmail(email);
        AuthorModel result;
        try{
            result = new AuthorModel(mongoModel);
            return ResponseEntity.ok().body(result);
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
//        return new AuthorModel(mongoModel.getName(), mongoModel.getEmail());
    }

    @RequestMapping("/authors")
    public List<AuthorModel> getAllAuthors(){
        //
        List<MongoAuthorModel> mongoModels = authorRepository.findAll();
        List<AuthorModel> result = new ArrayList<AuthorModel>();
        for(MongoAuthorModel mongoModel: mongoModels){
            result.add(new AuthorModel((mongoModel)));
        }
        return result;
    }

    @RequestMapping("author-registration")
    public ResponseEntity register(@Validated @RequestBody AuthorModel author){
        try{
            authorRepository.save(new MongoAuthorModel(author));
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.status(403).build();  // change status code to correct one
        }
    }



}

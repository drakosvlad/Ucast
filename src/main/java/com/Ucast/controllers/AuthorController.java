package com.Ucast.controllers;

import com.Ucast.repositories.AuthorRepository;
import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.AuthorModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @RequestMapping("/author/name/{name}")
    public ResponseEntity<AuthorModel> getAuthorInfo(@PathVariable("name") String name){
        MongoAuthorModel mongoModel = authorRepository.findByName(name);
        AuthorModel result;
        try{
            result = new AuthorModel(mongoModel);
            return ResponseEntity.ok().body(result);
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
//        return new AuthorModel(mongoModel.getName(), mongoModel.getEmail());
    }

    @RequestMapping("/author/id/{id}")
    public ResponseEntity<AuthorModel> getAuthorInfo(@PathVariable("id") ObjectId id){
        MongoAuthorModel mongoModel = authorRepository.findById(id);
        AuthorModel result;
        try{
            result = new AuthorModel(mongoModel);
            return ResponseEntity.ok().body(result);
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/authors")
    public List<MongoAuthorModel> getAllAuthors(){
        return authorRepository.findAll();
//        List<MongoAuthorModel> mongoModels = authorRepository.findAll();
//        List<AuthorModel> result = new ArrayList<>();
//        for(MongoAuthorModel mongoModel: mongoModels){
//            result.add(new AuthorModel((mongoModel)));
//        }
//        return result;
    }

    @RequestMapping("author-registration")
    public ResponseEntity register(@Validated @RequestBody AuthorModel author){
//        Optional<MongoAuthorModel> mongoAuthor = Optional.ofNullable(authorRepository.findByEmail(author.getEmail()));
//        if(mongoAuthor.isPresent()){
//            return ResponseEntity.status(403).build();  // change status code to correct one
//        }
//        authorRepository.save(new MongoAuthorModel(author));
//        return ResponseEntity.created(URI.create(String.format("/author/%s", author.getName()))).build();
        return ResponseEntity.status(403).build();
    }
}

package com.Ucast.controllers;

import com.Ucast.models.AuthorModel;
import com.Ucast.models.MongoUserModel;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.models.MongoAuthorModel;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/author/name/{name}")
    public ResponseEntity<MongoAuthorModel> getAuthorInfo(@PathVariable("name") String name){
        MongoAuthorModel authorModel = authorRepository.findByName(name);
        Optional<MongoAuthorModel> check = Optional.ofNullable(authorModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorModel);
//        return new AuthorModel(mongoModel.getName(), mongoModel.getEmail());
    }

    @RequestMapping("/author/id/{id}")
    public ResponseEntity<MongoAuthorModel> getAuthorInfo(@PathVariable("id") ObjectId id){
        MongoAuthorModel authorModel = authorRepository.findById(id);
        Optional<MongoAuthorModel> check = Optional.ofNullable(authorModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorModel);
    }

    @GetMapping("/channel/{channelName}")
    public ResponseEntity getChannelInfo(@PathVariable("channelName") String channelName){
        MongoAuthorModel author = authorRepository.findByChannelName(channelName);
        Optional<MongoAuthorModel> check = Optional.ofNullable(author);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

//    @RequestMapping("/authors")
//    public ResponseEntity<List<MongoAuthorModel>> getAllAuthors(Authentication authentication) {
//        return ResponseEntity.ok().body(authorRepository.findAll());
//    }

    @Secured("ROLE_USER")
    @PostMapping("/author-registration")
    public ResponseEntity register(@RequestBody AuthorModel authorModel){
        try{
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            MongoUserModel userModel = userRepository.findByEmail(login);
            Optional<MongoUserModel> check = Optional.ofNullable(userModel);
            if(check.isEmpty()){
                return ResponseEntity.status(404).body("authentication failed, user not found");
            }

            MongoAuthorModel existingAuthor = authorRepository.findByUserId(userModel.getObjectId());
            Optional<MongoAuthorModel> checkAuthor = Optional.ofNullable(existingAuthor);
            if(checkAuthor.isPresent()){
                return ResponseEntity.status(403).body("user is already registered as author");
            }

            MongoAuthorModel newAuthor = new MongoAuthorModel(userModel);
            newAuthor.setChannelName(authorModel.getChannelName());
            newAuthor.setCoverURL(authorModel.getCoverURL());
            authorRepository.save(newAuthor);
            return ResponseEntity.created(URI.create(String.format("/author/%s", newAuthor.getName()))).build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

    }
}

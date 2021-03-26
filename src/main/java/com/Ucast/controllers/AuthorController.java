package com.Ucast.controllers;

import com.Ucast.models.MongoUserModel;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.AuthorModel;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<MongoAuthorModel>> getAllAuthors(Authentication authentication) {
        // ROLE VERIFICATION EXAMPLE
//        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//            return ResponseEntity.status(403).build();
//        }

        return ResponseEntity.ok().body(authorRepository.findAll());
//        List<MongoAuthorModel> mongoModels = authorRepository.findAll();
//        List<AuthorModel> result = new ArrayList<>();
//        for(MongoAuthorModel mongoModel: mongoModels){
//            result.add(new AuthorModel((mongoModel)));
//        }
//        return result;
    }

    @Secured("ROLE_USER")
    @RequestMapping("/author-registration")
    public ResponseEntity register(@Validated @RequestBody AuthorModel author){
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
            authorRepository.save(newAuthor);
            return ResponseEntity.created(URI.create(String.format("/author/%s", newAuthor.getName()))).build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

    }
}

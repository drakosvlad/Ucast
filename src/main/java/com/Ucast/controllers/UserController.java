package com.Ucast.controllers;

import com.Ucast.auth.JwtTokenProvider;
import com.Ucast.models.*;
import com.Ucast.repositories.AdminRepository;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PodcastRepository podcastRepository;

    @PostMapping(value = "/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity userRegister(@Valid @RequestBody UserRegistrationModel registrationModel) {
        try {
            MongoUserModel existingModel = userRepository.findByEmail(registrationModel.getEmail());
            Optional<MongoUserModel> check = Optional.ofNullable(existingModel);
            if (check.isPresent()) {
                return ResponseEntity.status(403).body("User already exists");
            }

            String encryptedPassword = passwordEncoder.encode(registrationModel.getPassword());
            MongoUserModel newUser = new MongoUserModel(registrationModel.getUsername(),
                    registrationModel.getEmail(), encryptedPassword, registrationModel.getAvatarUrl());

            newUser.setFavoritePodcasts(new ArrayList<>());
            newUser.setBlocked(false);
            userRepository.save(newUser);
            return ResponseEntity.created(URI.create(String.format("/user/%s", newUser.getUsername()))).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserLoginModel loginModel) {
        try {
            MongoUserModel mongoModel = userRepository.findByEmail(loginModel.getEmail());
            Optional<MongoUserModel> check = Optional.ofNullable(mongoModel);
            if (check.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if(mongoModel.isBlocked()) {
                return ResponseEntity.status(401).build();
            }
            List<String> userRoles = new ArrayList<>();
            userRoles.add("ROLE_USER");
//                String encryptedPassword = passwordEncoder.encode(loginModel.getPassword());
            MongoAuthorModel authorModel = authorRepository.findByUserId(mongoModel.getObjectId());
            Optional<MongoAuthorModel> checkAuthor = Optional.ofNullable(authorModel);
            MongoAdminModel adminModel = adminRepository.findByUserId(mongoModel.getObjectId());
            Optional<MongoAdminModel> checkAdmin = Optional.ofNullable(adminModel);
            if(checkAuthor.isPresent()){
                userRoles.add("ROLE_AUTHOR");
            }
            if(checkAdmin.isPresent()){
                userRoles.add("ROLE_ADMIN");
            }
            if (passwordEncoder.matches(loginModel.getPassword(), mongoModel.getPassword())) {
                var authToken = jwtTokenProvider.generateToken(mongoModel.getId());
                return ResponseEntity.ok(new Object() {
                                             public final String token = authToken;
                                             public final List<String> roles = userRoles;
                                             public final String id = mongoModel.getId();
                                         }
                );
            }
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Secured("ROLE_USER")
    @PostMapping("/user/edit")
    public ResponseEntity editUser(@Validated @RequestBody UserRegistrationModel editModel){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel userModel = userRepository.findByEmail(login);
        Optional<MongoUserModel> check = Optional.ofNullable(userModel);
        if(check.isEmpty()){
            return ResponseEntity.status(404).body("Authentication failed. User not found");
        }
        userModel.setAvatarUrl(editModel.getAvatarUrl());
        userModel.setUsername(editModel.getUsername());
//        userModel.setPassword(passwordEncoder.encode(editModel.getPassword()));
        userRepository.save(userModel);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_USER")
    @PostMapping("/favorite-podcast/add/{id}")
    public ResponseEntity addFavoritePodcast(@PathVariable("id") String podcastId){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        Optional<MongoUserModel> check = Optional.ofNullable(user);
        if(check.isEmpty()){
            return ResponseEntity.status(404).body("Authentication failed. User not found");
        }

        Optional<MongoPodcastModel> podcastCheck = podcastRepository.findById(podcastId);
        if(podcastCheck.isEmpty()){
            return ResponseEntity.status(404).body("Podcast not found");
        }
        user.addFavoritePoscast(new ObjectId(podcastId));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @PostMapping("/favorite-podcast/remove/{id}")
    public ResponseEntity removeFavoritePodcast(@PathVariable("id") String podcastId){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        // TODO check if this validation is necessary
//        Optional<MongoUserModel> check = Optional.ofNullable(user);
//        if(check.isEmpty()){
//            return ResponseEntity.status(404).body("Authentication failed. User not found");
//        }

        Optional<MongoPodcastModel> podcastCheck = podcastRepository.findById(podcastId);
        if(podcastCheck.isEmpty()){
            return ResponseEntity.status(404).body("Podcast not found");
        }
        user.removeFavoritePodcast(new ObjectId(podcastId));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/user-info")
    public ResponseEntity<MongoUserModel> getUserInfo(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel userModel = userRepository.findByEmail(login);
        Optional<MongoUserModel> check = Optional.ofNullable(userModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userModel);
    }


}


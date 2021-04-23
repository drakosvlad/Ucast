package com.Ucast.controllers;

import com.Ucast.models.*;
import com.Ucast.repositories.AdminRepository;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PodcastRepository podcastRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping("/show-all-users")
    public ResponseEntity getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/show-all-authors")
    public ResponseEntity getAllAuthors(){
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/approve/podcast/{id}")
    public ResponseEntity approvePodcast(@PathVariable("id") String podcastId){
        MongoPodcastModel mongoModel = podcastRepository.findById(new ObjectId(podcastId));
        Optional<MongoPodcastModel> check = Optional.ofNullable(mongoModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        mongoModel.setChecked(true);
        podcastRepository.save(mongoModel);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/disapprove/podcast/{id}")
    public ResponseEntity disapprovePodcast(@PathVariable("id") String podcastId){
        MongoPodcastModel mongoModel = podcastRepository.findById(new ObjectId(podcastId));
        Optional<MongoPodcastModel> check = Optional.ofNullable(mongoModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        MongoPodcastModel deletedPodcast = podcastRepository.deleteById(new ObjectId(podcastId));
        MongoAuthorModel author = authorRepository.findById(deletedPodcast.getAuthorId());
        author.getPodcastIdList().remove(new ObjectId(podcastId));
        authorRepository.save(author);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/approve/author/{id}")
    public ResponseEntity approveAuthor(@PathVariable("id") String authorId){
        MongoAuthorModel mongoModel = authorRepository.findById(new ObjectId(authorId));
        Optional<MongoAuthorModel> check = Optional.ofNullable(mongoModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        mongoModel.setConfirmed(true);
        List<MongoPodcastModel> podcasts = podcastRepository.findAllByAuthorId(new ObjectId(authorId));
        if(!podcasts.isEmpty()){
            for(MongoPodcastModel podcast: podcasts){
                podcast.setChecked(true);
                podcastRepository.save(podcast);
            }
        }
        authorRepository.save(mongoModel);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/disapprove/author/{id}")
    public ResponseEntity disapproveAuthor(@PathVariable("id") String authorId){
        MongoAuthorModel mongoModel = authorRepository.findById(new ObjectId(authorId));
        Optional<MongoAuthorModel> check = Optional.ofNullable(mongoModel);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        authorRepository.deleteById(authorId);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/block-author/{id}")
    public ResponseEntity blockAuthor(@PathVariable("id") String authorId){
        MongoAuthorModel author = authorRepository.findById(new ObjectId(authorId));
        Optional<MongoAuthorModel> check = Optional.ofNullable(author);
        if(check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        author.setConfirmed(false);
        authorRepository.save(author);
        List<MongoPodcastModel> podcasts = podcastRepository.findAllByAuthorId(new ObjectId(authorId));
        for(MongoPodcastModel podcast: podcasts){
            podcast.setChecked(false);
            podcastRepository.save(podcast);
        }
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_USER")
    @PostMapping("/register-admin")
    public ResponseEntity registerAdmin(){
        try{
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            MongoUserModel userModel = userRepository.findByEmail(login);
            Optional<MongoUserModel> check = Optional.ofNullable(userModel);
            if(check.isEmpty()){
                return ResponseEntity.status(404).body("authentication failed, user not found");
            }
            MongoAdminModel existingAdmin = adminRepository.findByUserId(userModel.getObjectId());
            Optional<MongoAdminModel> check2 = Optional.ofNullable(existingAdmin);
            if(check2.isPresent()){
                return ResponseEntity.status(403).body("User is already admin");
            }
            MongoAdminModel admin = new MongoAdminModel(userModel);
            adminRepository.save(admin);
            return ResponseEntity.status(201).build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok("d,ldl,dcl,sdl,sd");
    }

    @Secured("ROLE_USER")
    @PostMapping("/block-user/{userId}")
    public ResponseEntity blockUser(@PathVariable("userId") String userId){
        Optional<MongoUserModel> userCheck = userRepository.findById(userId);
        if(userCheck.isEmpty())
            return ResponseEntity.status(404).body("user not found");
        MongoUserModel user = userCheck.get();
        if(user.isBlocked()){
            return ResponseEntity.status(403).body("user is already blocked");
        }
        user.setBlocked(true);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @PostMapping("/unblock-user/{userId}")
    public ResponseEntity unblockUser(@PathVariable("userId") String userId){
        Optional<MongoUserModel> userCheck = userRepository.findById(userId);
        if(userCheck.isEmpty())
            return ResponseEntity.status(404).body("user not found");
        MongoUserModel user = userCheck.get();
        if(!user.isBlocked()){
            return ResponseEntity.status(403).body("user is already active");
        }
        user.setBlocked(false);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}

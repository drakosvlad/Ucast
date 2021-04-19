package com.Ucast.controllers;

import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoPodcastModel;
import com.Ucast.models.MongoUserModel;
import com.Ucast.models.UserLoginModel;
import com.Ucast.repositories.AdminRepository;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @GetMapping("/show-all-admins")
    public ResponseEntity getAllAdmins(){
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/approve-podcast")
    public ResponseEntity approvePodcast(@RequestBody String podcastId){
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
    @PostMapping("/disapprove-podcast")
    public ResponseEntity disapprovePodcast(@RequestBody String podcastId){
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
}

package com.Ucast.controllers;

import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoPodcastModel;
import com.Ucast.models.MongoUserModel;
import com.Ucast.models.PodcastModel;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PodcastController {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/podcasts")
    public List<MongoPodcastModel> getAllPodcasts(){
        return podcastRepository.findAll();
    }

    @RequestMapping("/podcast/{id}")
    public ResponseEntity<PodcastModel> getPodcastInfo(@PathVariable("id") ObjectId id){
        MongoPodcastModel mongoModel = podcastRepository.findById(id);
        PodcastModel result;
        try{
            result = new PodcastModel(mongoModel);
            return ResponseEntity.ok().body(result);
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
//        return new AuthorModel(mongoModel.getName(), mongoModel.getEmail());
    }

    @Secured("ROLE_AUTHOR")
    @PostMapping("/add-podcast")
    public ResponseEntity createPodcast(@Validated @RequestBody PodcastModel model){
        MongoPodcastModel existingModel = podcastRepository.findByAuthorNameAndName(model.getAuthorName(), model.getName());
        Optional<MongoPodcastModel> check = Optional.ofNullable(existingModel);
        if(check.isPresent()){
            return ResponseEntity.status(403).body("Podcast with that name already exists");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectId userId = userRepository.findByEmail(email).getObjectId();
        MongoAuthorModel author = authorRepository.findByUserId(userId);
        ObjectId authorId = author.getObjectId();

        MongoPodcastModel newPodcast = new MongoPodcastModel(model);
        newPodcast.setChecked(false);
        newPodcast.setAuthorId(authorId);
        podcastRepository.save(newPodcast);

        MongoPodcastModel savedPodcast = podcastRepository.findByAuthorIdAndName(authorId, model.getName());
        author.addPodcast(savedPodcast.getObjectId());
        authorRepository.save(author);

        return ResponseEntity.created(URI.create(String.format("/podcast/%s", newPodcast.getId()))).build();
    }

    @Secured("ROLE_AUTHOR")
    @PostMapping("/edit-podcast")
    public ResponseEntity editPodcast(@Validated @RequestBody PodcastModel model){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        ObjectId podcastId;
        try{
            podcastId = new ObjectId(model.getId());
        }catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

        MongoAuthorModel author = authorRepository.findByUserId(user.getObjectId());
        Optional<MongoAuthorModel> authorCheck = Optional.ofNullable(author);
        if(authorCheck.isEmpty()){
            return ResponseEntity.status(404).body("Authentication failed. Author not found");
        }

        boolean existsInList = author.getPodcastIdList().contains(podcastId);
        MongoPodcastModel podcast = podcastRepository.findById(podcastId);
        Optional<MongoPodcastModel> podcastCheck = Optional.ofNullable(podcast);
        if(podcastCheck.isEmpty() || !existsInList){
            return ResponseEntity.status(404).body("Podcast not found");
        }

        podcast.setDescription(model.getDescription());
//        podcast.setFilepath(model.getFilepath());    // uncomment when filepath in static will be able to change
        podcast.setName(model.getName());
        podcastRepository.save(podcast);
        return ResponseEntity.ok("Podcast info changed");
    }

//    public ResponseEntity deletePodcast(){
//
//    }
}

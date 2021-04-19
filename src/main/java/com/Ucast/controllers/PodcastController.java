package com.Ucast.controllers;

import com.Ucast.models.*;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
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
//
//    @Autowired
//    private ReviewRepository reviewRepository;

    @GetMapping("/podcasts/all")
    public ResponseEntity getAllPodcasts() {
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return ResponseEntity.ok(podcastRepository.findAll());
        }
        return ResponseEntity.ok(podcastRepository.findAllByCheckedTrue());
    }

    @GetMapping("/podcasts/filtered")
    public ResponseEntity getFilteredPodcasts(@RequestBody SearchParameters searchParameters) {
        boolean isAdmin = false;
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            isAdmin = true;
           }
        String channelName = searchParameters.getChannelName();
        String podcastName = searchParameters.getPodcastName();
        if(!channelName.equals("")){
            MongoAuthorModel author = authorRepository.findByChannelName(channelName);
            Optional<MongoAuthorModel> check = Optional.ofNullable(author);
            if(check.isEmpty()){
                return ResponseEntity.status(404).body("Channel does not exist in database");
            }
            String authorName = author.getName();

           if(!podcastName.equals("")){
               MongoPodcastModel result = podcastRepository.findByAuthorNameAndName(authorName, podcastName);
               Optional<MongoPodcastModel> check2 = Optional.ofNullable(result);
               if(check2.isEmpty()){
                   return ResponseEntity.ok(new ArrayList<>());
               }
               if(result.isChecked() || isAdmin) {
                   return ResponseEntity.ok(result);
               } else {
                   return ResponseEntity.status(403).body("Podcast is not available");
               }

           }else{
               if (isAdmin)
                   return ResponseEntity.ok(podcastRepository.findByAuthorName(authorName));
               else
                   return ResponseEntity.ok(podcastRepository.findByAuthorNameAndCheckedTrue(authorName));
           }

        }else{
            if(!podcastName.equals("")){
                if (isAdmin)
                    return ResponseEntity.ok(podcastRepository.findAllByName(podcastName));
                else
                    return ResponseEntity.ok(podcastRepository.findAllByNameAndCheckedTrue(podcastName));
            }else{
                if (isAdmin)
                    return ResponseEntity.ok(podcastRepository.findAll());
                return ResponseEntity.ok(podcastRepository.findAllByCheckedTrue());
            }
        }
    }


    @GetMapping("/podcast/{id}")
    public ResponseEntity<MongoPodcastModel> getPodcastInfo(@PathVariable("id") String id){
        MongoPodcastModel mongoModel = podcastRepository.findById(new ObjectId(id));
        Optional<MongoPodcastModel> check = Optional.ofNullable(mongoModel);
        if(check.isPresent()){
            return ResponseEntity.ok(mongoModel);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured("ROLE_AUTHOR")
    @PostMapping("/podcast/add")
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

        // add new model to db
        MongoPodcastModel newPodcast = new MongoPodcastModel(model);
        newPodcast.setChecked(false);
        newPodcast.setAuthorId(authorId);
        newPodcast.setAuthorName(author.getName());
        newPodcast.setListened(0);
        newPodcast.setRating(0);
        newPodcast.initReviews();
        podcastRepository.save(newPodcast);

        // add podcast to author's list
        MongoPodcastModel savedPodcast = podcastRepository.findByAuthorIdAndName(authorId, model.getName());
        author.addPodcast(savedPodcast.getObjectId());
        authorRepository.save(author);

        return ResponseEntity.created(URI.create(String.format("/podcast/%s", newPodcast.getId()))).build();
    }

    @Secured("ROLE_AUTHOR")
    @PostMapping("/podcast/edit")
    public ResponseEntity editPodcast(@Validated @RequestBody PodcastModel model){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        ObjectId podcastId;
        // check if input model is correct
        try{
            podcastId = new ObjectId(model.getId());
        }catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

        // authorization validation
        MongoAuthorModel author = authorRepository.findByUserId(user.getObjectId());
        Optional<MongoAuthorModel> authorCheck = Optional.ofNullable(author);
        if(authorCheck.isEmpty()){
            return ResponseEntity.status(404).body("Authentication failed. Author not found");
        }

        // finds podcast model in db
        boolean existsInList = author.getPodcastIdList().contains(podcastId);
        MongoPodcastModel podcast = podcastRepository.findById(podcastId);
        Optional<MongoPodcastModel> podcastCheck = Optional.ofNullable(podcast);
        if(podcastCheck.isEmpty() || !existsInList){
            return ResponseEntity.status(404).body("Podcast not found");
        }
        //  change podcast info
        podcast.setDescription(model.getDescription());
//        podcast.setFilepath(model.getFilepath());    // uncomment when filepath in static will be able to change
        podcast.setName(model.getName());
        podcast.setPhotoURL(model.getPhotoURL());
        podcastRepository.save(podcast);
        return ResponseEntity.ok("Podcast info changed");
    }


//    @Secured("ROLE_USER")
    @PostMapping("/increment-listened/{id}")
    public ResponseEntity incrementListenedCounter(@PathVariable("id") String id){
        // in case input value should be
//        ObjectId podcastId = new ObjectId(id);
//        try{
//            podcastId = new ObjectId(id);
//        }catch (NullPointerException e){
//            e.printStackTrace();
//            return ResponseEntity.unprocessableEntity().build();
//        }
        Optional<MongoPodcastModel> check = podcastRepository.findById(id);
        if (check.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        MongoPodcastModel podcastModel = check.get();
        podcastModel.addListened();
        podcastRepository.save(podcastModel);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @GetMapping("/get-favorite-podcasts")
    public ResponseEntity getFavoritePodcasts(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        List<ObjectId> podcastIds = user.getFavoritePodcasts();
        ArrayList<MongoPodcastModel> result = new ArrayList<>();
        for(ObjectId podcastId:podcastIds){
            MongoPodcastModel podcast = podcastRepository.findById(podcastId);
            try{
                result.add(podcast);
            }catch (NullPointerException ignored){
            }
        }
        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_USER")
    @PostMapping("/add-review/{podcastId}")
    public ResponseEntity addReview(@PathVariable("podcastId") String podcastId, @RequestBody ReviewModel review){
        if(review.getRate() < 0 || review.getRate() > 5){
            return ResponseEntity.unprocessableEntity().build();
        }
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);

        // find podcast in db
        MongoPodcastModel podcast = podcastRepository.findById(new ObjectId(podcastId));
        Optional<MongoPodcastModel> podcastCheck = Optional.ofNullable(podcast);
        if(podcastCheck.isEmpty()){
            return ResponseEntity.status(404).body("Podcast not found");
        }

        review.setUserId(user.getId());
        podcast.addReview(review);
        podcastRepository.save(podcast);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getReviews/{podcastId}")
    public ResponseEntity getReviews(@PathVariable("podcastId") String podcastId){
        // find podcast in db
        MongoPodcastModel podcast = podcastRepository.findById(new ObjectId(podcastId));
        Optional<MongoPodcastModel> podcastCheck = Optional.ofNullable(podcast);
        if(podcastCheck.isEmpty()){
            return ResponseEntity.status(404).body("Podcast not found");
        }

        return ResponseEntity.ok(podcast.getReviews());
    }




//    public ResponseEntity deletePodcast(){
//
//    }
}

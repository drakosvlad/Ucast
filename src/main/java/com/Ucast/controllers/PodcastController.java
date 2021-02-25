package com.Ucast.controllers;

import com.Ucast.models.MongoPodcastModel;
import com.Ucast.models.PodcastModel;
import com.Ucast.repositories.PodcastRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PodcastController {

    @Autowired
    private PodcastRepository podcastRepository;

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

    @RequestMapping("/add-podcast")
    public void createPodcast(@Validated @RequestBody MongoPodcastModel model){     //????????
    }
}

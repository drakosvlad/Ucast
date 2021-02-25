package com.Ucast.controllers;

import com.Ucast.models.MongoPodcastModel;
import com.Ucast.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @RequestMapping("/add-podcast")
    public void createPodcast(@Validated @RequestBody MongoPodcastModel model){     //????????
    }
}

package com.Ucast.controllers;

import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoPodcastModel;
import com.Ucast.models.MongoUserModel;
import com.Ucast.models.UserRegistrationModel;
import com.Ucast.repositories.PodcastRepository;
import com.Ucast.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class StaticController {
    @Value("${storage.path}")
    private String storagePath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PodcastRepository podcastRepository;

    @Secured("ROLE_AUTHOR")
    @PostMapping("/static")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        Path root = Paths.get(storagePath);

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUserModel user = userRepository.findByEmail(login);
        Optional<MongoUserModel> userCheck = Optional.ofNullable(user);
        if(userCheck.isEmpty()){
            return ResponseEntity.status(404).body("User not found");
        }

        String authorName = user.getUsername();
        String filename = file.getOriginalFilename();
        MongoPodcastModel existingPodcast = podcastRepository.findByAuthorNameAndName(authorName, filename);
        Optional<MongoPodcastModel> podcastCheck = Optional.ofNullable(existingPodcast);
        if(podcastCheck.isPresent()){
            return ResponseEntity.status(403).body("file with specified name already exists in user podcasts' list");
        }

        String newFileName = java.util.UUID.randomUUID() + "-" + filename;
        try {
            file.transferTo(root.resolve(newFileName).toFile());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not save file");
        }

        return ResponseEntity.ok("/static/" + newFileName);
    }

    @GetMapping("/static/{fileName}")
    public ResponseEntity<Object> handleFileDownload(@PathVariable("fileName") String fileName) {
        if (fileName.contains("..")) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(storagePath).resolve(fileName);
        String mimeType = URLConnection.guessContentTypeFromName(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mimeType);
        Resource resource =
                new FileSystemResource(filePath.toAbsolutePath());

        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }
}

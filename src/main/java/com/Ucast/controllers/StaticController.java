package com.Ucast.controllers;

import com.Ucast.models.MongoUserModel;
import com.Ucast.models.UserRegistrationModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/static")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        Path root = Paths.get(storagePath);
        String newFileName = java.util.UUID.randomUUID() + "-" + file.getOriginalFilename();

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

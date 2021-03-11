package com.Ucast.controllers;

import com.Ucast.auth.JwtTokenProvider;
import com.Ucast.models.MongoUserModel;
import com.Ucast.models.UserLoginModel;
import com.Ucast.models.UserRegistrationModel;
import com.Ucast.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/registration", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity userRegister(@Valid @RequestBody UserRegistrationModel registrationModel) {
        try {
            MongoUserModel existingModel = userRepository.findByEmail(registrationModel.getEmail());
            Optional<MongoUserModel> check = Optional.ofNullable(existingModel);
            if (check.isPresent()) {
                return ResponseEntity.status(403).build();
            } else {
                String encryptedPassword = passwordEncoder.encode(registrationModel.getPassword());
                MongoUserModel newUser = new MongoUserModel(registrationModel.getUsername(),
                        registrationModel.getEmail(), encryptedPassword, registrationModel.getAvatarUrl());
                userRepository.save(newUser);
                return ResponseEntity.created(URI.create(String.format("/user/%s", newUser.getUsername()))).build();
            }
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
            } else {
                String encryptedPassword = passwordEncoder.encode(loginModel.getPassword());

                if (passwordEncoder.matches(loginModel.getPassword(), mongoModel.getPassword())) {
                    var token = jwtTokenProvider.generateToken(mongoModel.getId());
                    return ResponseEntity.ok(token);
                }
                return ResponseEntity.status(401).build();  // TODO complete response with exception text?
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}


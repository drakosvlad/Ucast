package com.Ucast.controllers;

import com.Ucast.models.MongoUserModel;
import com.Ucast.models.UserLoginModel;
import com.Ucast.models.UserRegistrationModel;
import com.Ucast.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/registration")
    public ResponseEntity userRegister(@Valid @RequestBody UserRegistrationModel registrationModel){
        try{
            MongoUserModel existingModel = userRepository.findByEmail(registrationModel.getEmail());
            Optional<MongoUserModel> check = Optional.ofNullable(existingModel);
            if (check.isPresent()){
                return ResponseEntity.status(403).build();
            }else{
             String encryptedPassword = passwordEncoder.encode(registrationModel.getPassword());
             MongoUserModel newUser = new MongoUserModel(registrationModel.getUsername(),
                     registrationModel.getEmail(), encryptedPassword, registrationModel.getAvatarUrl());
             userRepository.save(newUser);
             return ResponseEntity.created(URI.create(String.format("/user/%s", newUser.getUsername()))).build();
            }
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @RequestMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserLoginModel loginModel){
        try{
            MongoUserModel mongoModel = userRepository.findByEmail(loginModel.getEmail());
            Optional<MongoUserModel> check = Optional.ofNullable(mongoModel);
            if(check.isEmpty()){
                return ResponseEntity.notFound().build();
            }else{
                String encryptedPassword = passwordEncoder.encode(loginModel.getPassword());
                if(encryptedPassword.equals(mongoModel.getPassword())){
                   return null; // TODO complete response with auth token (or no?)
                }
                return ResponseEntity.status(401).build();  // TODO complete response with exception text?
            }
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }



}


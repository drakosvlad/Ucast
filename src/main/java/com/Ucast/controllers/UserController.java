package com.Ucast.controllers;

import com.Ucast.models.MongoUserModel;
import com.Ucast.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("user-registration")
    public ResponseEntity userRegister(@Validated @RequestBody MongoUserModel user){
        return null;
    }
}

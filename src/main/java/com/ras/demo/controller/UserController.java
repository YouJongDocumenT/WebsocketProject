package com.ras.demo.controller;

import com.ras.demo.dto.User;
import com.ras.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/aa")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("로직 진행 중");
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}


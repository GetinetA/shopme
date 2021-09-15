package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Getinet on 9/14/21
 */
@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(String email) {
        return userService.isEmailUnique(email)? "OK":"Duplicated";
    }
}

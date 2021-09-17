package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public String checkDuplicateEmail(@Param(value = "id") Integer id,
                                      @Param(value = "email") String email) {
        return userService.isEmailUnique(id, email)? "OK":"Duplicated";
    }
}

package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.user.UserService;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by Getinet on 9/13/21
 */
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listAll(Model model) {
        List<User> listOfUsers = userService.listAll();
        model.addAttribute("listOfUsers",listOfUsers);
        return "users";
    }
}

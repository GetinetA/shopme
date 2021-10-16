package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.user.UserService;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Getinet on 9/9/21
 */
@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }
/*

    @GetMapping("")
    public @ResponseBody List<User> viewHomePage() {
        Page<User> users = userService.listByPage(1, "firstName", UserService.SORT_ASC, null);

        return users.getContent();
    }
*/

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

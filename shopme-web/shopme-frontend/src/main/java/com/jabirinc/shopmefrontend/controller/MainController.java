package com.jabirinc.shopmefrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Getinet on 9/9/21
 */
@Controller
public class MainController {

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
}

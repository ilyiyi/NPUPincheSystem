package com.devhub.pinchesystemback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author awater
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public String register(){
        return "register.html";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login.html";
    }
}

package com.devhub.pinchesystemback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/orders")
    public String order() {
        return "orders";
    }

    @GetMapping("/publish")
    public String publish() {
        return "reservation";
    }

    @GetMapping("/orders/generate")
    public String orderPublish() {
        return "detail_yuyue";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/myInfo")
    public String myInfo() {
        return "myInfo";
    }
}

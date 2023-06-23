package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
@Slf4j
public class PageController {

    @Resource
    private RedisUtil redisUtil;

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
    public String myInfo(Model model) {
        User currentUser = getCurrentUser();
        log.info("currentUser");
        model.addAttribute("cur", currentUser);

        return "myInfo";
    }


    private User getCurrentUser() {
        return redisUtil.getCurrentUser("cur");
    }
}

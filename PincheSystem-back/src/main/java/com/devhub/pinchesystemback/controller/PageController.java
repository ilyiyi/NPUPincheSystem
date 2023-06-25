package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
@Slf4j
public class PageController {

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/")
    public String index1() {
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        setCur(model);
        return "index";
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('OWNER')")
    public String order() {
        return "orders";
    }

    @GetMapping("/publish")
    @PreAuthorize("hasRole('OWNER')")
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
        setCur(model);
        return "myInfo";
    }

    @GetMapping("/orderStatement")
    public String orderInfo(Model model) {
        setCur(model);
        return "orderStatement";
    }

    private void setCur(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("cur", currentUser);
    }

    private User getCurrentUser() {
        return redisUtil.getCurrentUser("cur");
    }
}

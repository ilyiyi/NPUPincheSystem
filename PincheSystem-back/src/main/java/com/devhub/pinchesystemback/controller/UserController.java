package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.pararm.RegisterParam;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author awater
 */
@Controller
@Slf4j
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String mobile = registerParam.getMobile();
        boolean flag = userService.register(username, password, mobile);
        if (flag) {
            return "/login";
        }

        return "/";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }
}

package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.LoginParam;
import com.devhub.pinchesystemback.pararm.RegisterParam;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.service.impl.UserServiceImpl;
import com.devhub.pinchesystemback.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;

/**
 * @author awater
 */
@Controller
//@Slf4j
//@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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
            return "login";
        }

        return "/";
    }

    @GetMapping(value = "login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    public String login(@Valid @RequestBody LoginParam loginParam, HttpServletResponse response) {
        User user = userService.login(loginParam.getUsername(),loginParam.getPassword());
        if(user != null){
            String token = jwtUtil.getTokenFromUser(user);
            response.setHeader("token",token);
            return "myInfo";
        }
        return "login";
    }

    @PostMapping("/submit-form")
    public ModelAndView handleFormSubmission(@RequestParam String name, @RequestParam String message) {
        ModelAndView mav = new ModelAndView("login"); // 视图名称
        mav.addObject("name", name);
        mav.addObject("message", message);
        return mav;
    }
}

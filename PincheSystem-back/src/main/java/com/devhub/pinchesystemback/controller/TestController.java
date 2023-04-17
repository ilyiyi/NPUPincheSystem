package com.devhub.pinchesystemback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller
public class TestController {
    @GetMapping(value = "hello")
    public String hello(){
        return "Hello,NPUers!";
    }
}

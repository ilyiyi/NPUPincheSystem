package com.devhub.pinchesystemback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author awater
 */
@Controller
public class TestController {
    @GetMapping(value = "hello")
    public String hello(){
        return "index";
    }
}

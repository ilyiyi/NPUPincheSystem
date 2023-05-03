package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.utils.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author awater
 */
@Controller
public class TestController {

    @Resource
    private RedisUtil redisUtil;

    @GetMapping(value = "hello")
    public String hello() {
        return "index";
    }

    @GetMapping("/set")
    public String sSet(Long... ids) {
        redisUtil.sSet("user:1", (Object) ids);
        return "index";
    }

}

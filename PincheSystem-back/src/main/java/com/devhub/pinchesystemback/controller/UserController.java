package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.advice.annotation.Log;
import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.constant.UserRoleEnum;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.exception.NotFoundException;
import com.devhub.pinchesystemback.pararm.LoginParam;
import com.devhub.pinchesystemback.pararm.ModifyParam;
import com.devhub.pinchesystemback.pararm.RegisterParam;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.utils.JwtUtil;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.CommonResult;
import com.devhub.pinchesystemback.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getCurUserName")
    @ResponseBody
    public CommonResult getCurUserId() {
        Object username
                = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (username == null) {
            return CommonResult.failure("获取失败！");
        }
        return CommonResult.success(username);
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String mobile = registerParam.getMobile();
        boolean flag = userService.register(username, password, mobile);
        if (flag) {
            return "redirect:/login";
        } else {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "用户名或密码不满足条件，请重试！");
        }
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/login")
    @Log
    public String login(@Valid LoginParam loginParam, HttpServletResponse response, HttpServletRequest request) {
        log.info("开始登录！");
        User user = userService.login(loginParam.getUsername(), loginParam.getPassword(), request);
        if (UserRoleEnum.ORDINARY_USER.getRole() == user.getRole()) {
            Cookie cookie = new Cookie("username", user.getUsername());
            response.addCookie(cookie);
            redisUtil.setObject("cur", user);
            log.info("登录成功！");
            return "myInfo";
        } else {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非普通用户账户无法在此登录");
        }
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    @ResponseBody
    public UserVO getOwnInfo(@AuthenticationPrincipal User user) {
        return userService.getInfo(user.getId());
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public UserVO getUserInfo(@PathVariable Long userId) {
        if (userService.isDeleted(userId)) {
            throw new NotFoundException("用户id为" + userId + "的用户不存在或已被删除");
        }
        return userService.getInfo(userId);
    }

    @PostMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    @ResponseBody
    public CommonResult modifyOwnInfo(@RequestBody ModifyParam param) {
        User user = redisUtil.getCurrentUser("cur");
        if (userService.modifyInfo(user.getId(), param)) {
            String username = param.getUsername();
            String sex = param.getSex();
            String mobile = param.getMobile();
            if (username != null) {
                user.setUsername(username);
            }
            if (sex != null) {
                user.setSex(sex);
            }
            if (mobile != null) {
                user.setMobile(mobile);
            }
            redisUtil.setObject("cur", user);
            return CommonResult.success();
        }
        return CommonResult.failure("更新失败，请稍后再试");
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER')")
    public String userLogout(HttpServletRequest request) {
        if (request != null) {
            boolean out = userService.userLogout(request);
            if (out) {
                return "login";
            }
        }
        return "error";
    }

}

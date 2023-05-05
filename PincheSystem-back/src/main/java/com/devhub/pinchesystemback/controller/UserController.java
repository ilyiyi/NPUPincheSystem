package com.devhub.pinchesystemback.controller;

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
import com.devhub.pinchesystemback.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginParam loginParam, HttpServletResponse response) {
        User user = userService.login(loginParam.getUsername(),loginParam.getPassword());
        if(UserRoleEnum.ORDINARY_USER.getRole() == user.getRole()){
            String token = jwtUtil.getTokenFromUser(user);
            response.setHeader("token",token);
            redisUtil.setObject("cur",user);
            return "myInfo";
        }else {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非普通用户账户无法在此登录");
        }
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    @ResponseBody
    public UserVO getOwnInfo(@AuthenticationPrincipal User user){
        return userService.getInfo(user.getId());
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public UserVO getUserInfo(@PathVariable Long userId) {
        if(userService.isDeleted(userId)){
            throw new NotFoundException("用户id为"+userId+"的用户不存在或已被删除");
        }
        return userService.getInfo(userId);
    }

    @PutMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    @ResponseBody
    public Object modifyOwnInfo(@AuthenticationPrincipal User user, ModifyParam modifyParam){
        userService.modifyInfo(user.getId(), modifyParam.getUsername(), modifyParam.getPassword(), modifyParam.getMobile(), modifyParam.getSex());
        return new UserVO();
    }
}

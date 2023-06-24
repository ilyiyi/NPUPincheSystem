package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.constant.UserRoleEnum;
import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.pararm.LoginParam;
import com.devhub.pinchesystemback.service.AdminService;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;


    @PostMapping("/login")
    public String login(@Valid LoginParam loginParam, HttpServletResponse response, HttpServletRequest request) {
        User user = userService.login(loginParam.getUsername(), loginParam.getPassword(), request);
        if (UserRoleEnum.ADMINISTRATOR.getRole() == user.getRole()) {
            response.setHeader("username", user.getUsername());
            redisUtil.setObject("curAdmin", user);
            return "deals";
        } else {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非普通用户账户无法在此登录");
        }
    }

    /**
     * 根据开始结束时间或车主id查询全部拼车记录
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @param id    车主id
     * @return 拼车记录
     */
    @GetMapping("/outRecord")
    public CommonResult recordsOut(Date begin, Date end, Long id) {
        List<Order> records = adminService.getAllRecords(begin, end, id);
        return CommonResult.success(records);
    }

    @PostMapping("/rank")
    public CommonResult ownerRank(Date begin, Date end) {

        if (end.before(begin)) {
            return CommonResult.failure("开始时间晚于结束时间!");
        }

        List<User> rank = adminService.getOwnerRank(begin, end);
        return CommonResult.success(rank);
    }
}

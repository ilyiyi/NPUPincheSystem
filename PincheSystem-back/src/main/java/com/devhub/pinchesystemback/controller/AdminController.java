package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.constant.UserRoleEnum;
import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.pararm.LoginParam;
import com.devhub.pinchesystemback.pararm.SearchParam;
import com.devhub.pinchesystemback.service.AdminService;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.CommonResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private RedisUtil redisUtil;


    @PostMapping("/login")
    public String login(@Valid LoginParam loginParam, HttpServletResponse response, HttpServletRequest request) {
        User user = userService.login(loginParam.getUsername(), loginParam.getPassword(), request);
        if (UserRoleEnum.ADMINISTRATOR.getRole() == user.getRole()) {
            response.setHeader("username", user.getUsername());
            redisUtil.setObject("cur", user);
            return "deals";
        } else {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "非普通用户账户无法在此登录");
        }
    }

    @PostMapping("/rank")
    public CommonResult ownerRank(@RequestBody SearchParam param) {
        Date begin = param.getBegin();
        Date end = param.getEnd();
        if (end.before(begin)) {
            return CommonResult.failure("开始时间晚于结束时间!");
        }
        List<Map.Entry<Double, User>> rank = orderService.getOwnerRank(begin, end);
        return CommonResult.success(rank);
    }

    @PostMapping("/search")
    public CommonResult searchOrders(@RequestBody SearchParam param) {
        Map.Entry<Double, User> entry = orderService.searchOwnerOrders(param);
        return CommonResult.success(entry);
    }
}

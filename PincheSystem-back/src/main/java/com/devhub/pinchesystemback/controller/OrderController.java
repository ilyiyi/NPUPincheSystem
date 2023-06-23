package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.advice.annotation.Log;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.OrderParam;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wak
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/generate")
    public CommonResult generateOrder(@RequestBody OrderParam param) {
        try {
            if (orderService.generateOrder(param)) {
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failure("预约失败，请稍后再试");
    }

    @GetMapping("/list")
    @Log
    public CommonResult listOrders() {
        User user = redisUtil.getCurrentUser("cur");
        List<Order> orders = orderService.getOrderListByUserId(user.getId());
        return CommonResult.success(orders);
    }

    @DeleteMapping("/{id}")
    public CommonResult deleteOrder(@PathVariable("id") Long id) {
        try {
            if (orderService.deleteOrder(id)) {
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failure("删除失败，请稍后再试");
    }

    @PostMapping("/update")
    public CommonResult updateOrder(@RequestBody Order order) {
        try {
            if (orderService.updateOrder(order)) {
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failure("更新失败，请稍后再试");
    }


    public User getCurrentUser() {
        return redisUtil.getCurrentUser("cur");
    }
}

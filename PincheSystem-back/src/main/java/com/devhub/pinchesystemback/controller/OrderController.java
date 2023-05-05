package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author wak
 */
@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/generate")
    @ResponseBody
    public boolean generateOrder(@RequestBody Order order) {
        try {
            User currentUser = getCurrentUser();
            String key = "user_" + currentUser.getId();
            orderService.saveOrder(order);
            log.info(order.getOrderId().toString());
            redisUtil.sSet(key, -1L, order.getOrderId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(Long id) {
        try {
            User currentUser = getCurrentUser();
            String key = "user_" + currentUser.getId();
            redisUtil.sRemove(key, id);
            orderService.deleteOrder(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrder(@RequestBody Order order) {
        try {

            return orderService.updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getCurrentUser() {
        return redisUtil.getCurrentUser("cur");
    }
}

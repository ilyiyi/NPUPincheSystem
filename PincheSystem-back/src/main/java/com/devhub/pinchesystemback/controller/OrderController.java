package com.devhub.pinchesystemback.controller;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author wak
 */
@Controller
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RedisUtil redisUtil;

    public boolean generateOrder(@RequestBody Order order) {
        try {
            User currentUser = getCurrentUser();
            String key = "user_" + currentUser.getId();
            Long orderId = orderService.saveOrder(order);
            redisUtil.sSet(key, orderId);
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
        return (User) MDC.get("currentUser");
    }
}

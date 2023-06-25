package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.mapper.UserOrderMapper;
import com.devhub.pinchesystemback.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class PincheSystemBackApplicationTests {

    @Resource
    private OrderService orderService;

    @Resource
    private UserOrderMapper userOrderMapper;

    @Test
    void contextLoads() {
        List<Order> orders = orderService.listValidOrders(26L);
        orders.forEach(System.out::println);
    }

    @Test
    public void testUserOrder() {
        userOrderMapper.getOrderIDsByUserId(26L);
    }
}

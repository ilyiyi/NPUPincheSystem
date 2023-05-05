package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class TestOrderMapper {
    @Resource
    private OrderMapper mapper;

    @Test
    public void testOrderInsert() {
        Order order = new Order();
        order.setEnding("end");
        order.setInfoId(1L);
        order.setPassengerNum((byte) 2);
        order.setStartTime(new Date());
        order.setPrice(10.0);
        order.setOrderState((byte) 1);
        order.setRemark("re");
        System.out.println(mapper.insert(order));
    }
}

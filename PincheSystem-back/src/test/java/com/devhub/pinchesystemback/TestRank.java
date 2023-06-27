package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import com.devhub.pinchesystemback.mapper.OwnerOrderMapper;
import com.devhub.pinchesystemback.mapper.UserMapper;
import com.devhub.pinchesystemback.pararm.RankParam;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRank {
    @Resource
    private UserMapper userMapper;

    @Resource
    private OwnerOrderMapper ownerOrderMapper;

    @Resource
    private OrderMapper mapper;

    public List<Order> listValidOrdersForOwner(Long userId) {
        List<Long> ids = ownerOrderMapper.selectOrderIdsByOwnerId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 2);
    }


    public List<Order> listUnValidOrders(Long userId) {
        List<Long> ids = ownerOrderMapper.selectOrderIdsByOwnerId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 1);
    }

    @Test
    public void test() {
        // 先查出所有车主
        List<User> owners = userMapper.selectAllOwners();
        Map<Double, List<RankParam>> map = new HashMap<>();
        for (User owner : owners) {
            Long ownerId = owner.getId();
            List<Order> validOrders = listValidOrdersForOwner(ownerId);
            List<Order> unValidOrders = listUnValidOrders(ownerId);

            if (validOrders.size() != 0 || unValidOrders.size() != 0) {
                BigDecimal total = new BigDecimal(validOrders.size() + unValidOrders.size());
                BigDecimal success = new BigDecimal(validOrders.size());
                double result = success.divide(total, 2, RoundingMode.HALF_UP).doubleValue();
                RankParam rankParam = new RankParam();
                rankParam.setUser(owner);
                rankParam.setSuccessRate(result);
                rankParam.setCount(validOrders.size());
                List<RankParam> list = map.getOrDefault(result, new ArrayList<>());
                list.add(rankParam);
                map.put(result, list);
            }

        }
        System.out.println(map);
    }
}

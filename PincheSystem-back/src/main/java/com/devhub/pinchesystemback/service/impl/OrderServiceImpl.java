package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import com.devhub.pinchesystemback.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wak
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper mapper;

    /**
     * 保存订单
     *
     * @param order 订单信息
     */
    @Override
    public void saveOrder(Order order) {
        mapper.insert(order);
    }

    /**
     * 更新订单
     *
     * @param order 更新的订单信息
     * @return 是否成功
     */
    @Override
    public boolean updateOrder(Order order) {
        return mapper.updateByOrderId(order) > 0;
    }

    /**
     * 根据订单id删除订单
     *
     * @param id 订单id
     */
    @Override
    public void deleteOrder(Long id) {
        mapper.deleteByOrderId(id);
    }

    /**
     * 分页查询订单
     *
     * @param pageSize    每页数据条数
     * @param currentPage 当前页
     * @return 页数据信息
     */
    @Override
    public PageInfo<Order> selectOrders(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Order> orders = mapper.selectAll();
        return new PageInfo<>(orders);
    }
}

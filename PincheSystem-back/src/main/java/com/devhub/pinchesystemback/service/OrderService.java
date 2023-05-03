package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.Order;
import com.github.pagehelper.PageInfo;

/**
 * @author wak
 */
public interface OrderService {

    /**
     * 保存订单
     *
     * @param order 订单信息
     * @return 是否成功
     */
    Long saveOrder(Order order);

    /**
     * 更新订单
     *
     * @param order 更新的订单信息
     * @return 是否成功
     */
    boolean updateOrder(Order order);

    /**
     * 根据订单id删除订单
     *
     * @param id 订单id
     * @return 是否成功
     */
    boolean deleteOrder(Long id);

    /**
     * 分页查询订单
     *
     * @param pageSize    每页数据条数
     * @param currentPage 当前页
     * @return 页数据信息
     */
    PageInfo<Order> selectOrders(int currentPage, int pageSize);
}

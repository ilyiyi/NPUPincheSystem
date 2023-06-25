package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.pararm.OrderParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author wak
 */
public interface OrderService {

    /**
     * 保存订单
     *
     * @param order 订单信息
     */
    boolean saveOrder(Order order);

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
     */
    boolean deleteOrder(Long id);

    Order getOrder(Long orderId);

    /**
     * 分页查询订单
     *
     * @param pageSize    每页数据条数
     * @param currentPage 当前页
     * @return 页数据信息
     */
    PageInfo<Order> selectOrders(int currentPage, int pageSize);

    PageInfo<List<Order>> selectOrders(List<Long> infoIds, int currentPage, int pageSize);

    /**
     * 审核订单
     */
    void reviewOrder(Long orderId,Byte orderState);

    List<Order> getOrderListByUserId(Long userId);

    boolean generateOrder(OrderParam param);

    /**
     * 查询审核过的订单
     * @param userId 当前用户id
     * @return
     */
    List<Order> listValidOrders(Long userId);
}

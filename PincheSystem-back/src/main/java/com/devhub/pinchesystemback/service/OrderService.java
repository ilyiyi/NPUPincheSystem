package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.OrderParam;
import com.devhub.pinchesystemback.pararm.SearchParam;
import com.github.pagehelper.PageInfo;


import java.util.Date;
import java.util.List;
import java.util.Map;

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
    void reviewOrder(Long orderId, Byte orderState);

    List<Order> getOrderListByUserId(Long userId);

    boolean generateOrder(OrderParam param);

    /**
     * 查询审核过的订单
     *
     * @param userId 当前用户id
     * @return
     */
    List<Order> listValidOrders(Long userId);

    /**
     * 查询未审核过的订单
     *
     * @param userId 车主id
     * @return
     */
    List<Order> listUnValidOrders(Long userId);

    /**
     * 根据车主id查询需要审核的订单
     *
     * @param ownerId 车主id
     * @return
     */
    List<Order> listReviewOrderList(Long ownerId);

    /**
     * 获取车主排名
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return
     */
    List<Map.Entry<Double, User>> getOwnerRank(Date begin, Date end);

    /**
     * 查询满足条件的订单
     *
     * @param param 搜索参数
     * @return
     */
    Map.Entry<Double, User> searchOwnerOrders(SearchParam param);


}

package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.domain.UserOrder;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.mapper.InfoMapper;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import com.devhub.pinchesystemback.mapper.UserOrderMapper;
import com.devhub.pinchesystemback.pararm.OrderParam;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wak
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper mapper;

    @Resource
    private InfoMapper infoMapper;

    @Resource
    private UserOrderMapper userOrderMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 保存订单
     *
     * @param order 订单信息
     */
    @Override
    public boolean saveOrder(Order order) {
        Long infoId = order.getInfoId();
        Info info = infoMapper.selectByPrimaryKey(infoId);
        if (info != null) {
            int remain = info.getRemain() - order.getPassengerNum();
            if (remain >= 0) {
                return mapper.insert(order) > 0;
            } else {
                throw new BusinessException(ResultCodeEnum.SEAT_NOT_ENOUGH, "座位余量不足，请换车或减少拼车人数！");
            }
        } else {
            throw new BusinessException(ResultCodeEnum.INFO_NOT_EXIST, "拼车信息不存在，请重试！");
        }
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
    public boolean deleteOrder(Long id) {
        return mapper.deleteByOrderId(id) > 0;
    }

    /**
     * @param orderId 订单id
     * @return 订单
     */
    @Override
    public Order getOrder(Long orderId) {
        return mapper.getOrderByOrderId(orderId);
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

    /**
     * @param infoIds     订单id
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return 订单列表
     */
    @Override
    public PageInfo<List<Order>> selectOrders(List<Long> infoIds, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<List<Order>> orders = new ArrayList<>();
        for (Long infoId : infoIds) {
            orders.add(mapper.selectAllByInfoId(infoId));
        }
        return new PageInfo<>(orders);
    }


    /**
     * 审核订单
     *
     * @param orderId    订单id
     * @param orderState 订单状态
     */
    @Override
    public void reviewOrder(Long orderId, Byte orderState) {
        Order order = mapper.getOrderByOrderId(orderId);
        if (order == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_VALIDATE_FAILED, "订单不存在！");
        }
        /*修改订单状态*/
        order.setOrderState(orderState);
        mapper.updateByOrderId(order);
        /*修改拼车信息*/
        Info info = infoMapper.selectByPrimaryKey(order.getInfoId());
        info.setRemain((byte) (info.getRemain() - order.getPassengerNum()));
        infoMapper.updateByPrimaryKey(info);
    }

    @Override
    public List<Order> getOrderListByUserId(Long userId) {
        List<Long> ids = userOrderMapper.getOrderIDsByUserId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIds(ids);
    }

    @Override
    public boolean generateOrder(OrderParam param) {

        User currentUser = getCurrentUser();

        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(currentUser.getId());
        userOrder.setOrderId(param.getOrderId());

        Order order = new Order();
        order.setPassengerNum(param.getPassengerNum());
        order.setEnding(param.getEnding());
        order.setInfoId(param.getInfoId());
        order.setStartTime(param.getStartTime());
        order.setPrice(param.getPrice());
        order.setOrderState(param.getOrderState());
        order.setRemark(param.getRemark());

        return userOrderMapper.insert(userOrder) > 0 && saveOrder(order);
    }

    public User getCurrentUser() {
       return  redisUtil.getCurrentUser("cur");
    }
}

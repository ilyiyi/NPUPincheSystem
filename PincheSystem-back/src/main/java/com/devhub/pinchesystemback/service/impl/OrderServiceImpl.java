package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.domain.*;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.mapper.*;
import com.devhub.pinchesystemback.pararm.OrderParam;
import com.devhub.pinchesystemback.pararm.RankParam;
import com.devhub.pinchesystemback.pararm.SearchParam;
import com.devhub.pinchesystemback.service.OrderService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    private OwnerOrderMapper ownerOrderMapper;

    @Resource
    private UserMapper userMapper;

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
                info.setRemain((byte) remain);
                infoMapper.updateByPrimaryKey(info);
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
        if (orderState == 1) {
            Info info = infoMapper.selectByPrimaryKey(order.getInfoId());
            info.setRemain((byte) (info.getRemain() + order.getPassengerNum()));
            infoMapper.updateByPrimaryKey(info);
        }
    }

    @Override
    public List<Order> getOrderListByUserId(Long userId) {
        List<Long> ids = userOrderMapper.getOrderIDsByUserId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 0);
    }

    @Override
    public boolean generateOrder(OrderParam param) {
        // 获取当前登录的用户信息
        User currentUser = getCurrentUser();

        // 当前用户生成的订单
        Order order = new Order();
        order.setPassengerNum(param.getPassengerNum());
        order.setEnding(param.getEnding());
        order.setInfoId(param.getInfoId());
        order.setStartTime(param.getStartTime());
        order.setPrice(param.getPrice());
        order.setOrderState(param.getOrderState());
        order.setRemark(param.getRemark());

        // 根据生成订单的infoId查询对应的车主id
        Info info = infoMapper.selectByPrimaryKey(param.getInfoId());
        Long ownerId = info.getOwnerId();

        // 生成订单后，将用户，车主与订单关联
        if (saveOrder(order)) {
            UserOrder userOrder = new UserOrder();
            userOrder.setUserId(currentUser.getId());
            userOrder.setOrderId(order.getOrderId());

            OwnerOrder ownerOrder = new OwnerOrder();
            ownerOrder.setOwnerId(ownerId);
            ownerOrder.setOrderId(order.getOrderId());

            return userOrderMapper.insert(userOrder) > 0 && ownerOrderMapper.insert(ownerOrder) > 0;
        }

        return false;
    }

    /**
     * 用户查询审核过的订单
     *
     * @param userId 当前用户id
     * @return
     */
    @Override
    public List<Order> listValidOrders(Long userId) {
        List<Long> ids = userOrderMapper.getOrderIDsByUserId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 2);
    }


    /**
     * 车主查询未成功的订单
     *
     * @param userId 车主id
     * @return
     */
    @Override
    public List<Order> listUnValidOrders(Long userId) {
        List<Long> ids = ownerOrderMapper.selectOrderIdsByOwnerId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 1);
    }

    /**
     * 车主查询成功的订单
     *
     * @param userId 车主id
     * @return
     */
    @Override
    public List<Order> listValidOrdersForOwner(Long userId) {
        List<Long> ids = ownerOrderMapper.selectOrderIdsByOwnerId(userId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 2);
    }

    /**
     * 根据车主id查询需要审核的订单
     *
     * @param ownerId 车主id
     * @return
     */
    @Override
    public List<Order> listReviewOrderList(Long ownerId) {
        List<Long> ids = ownerOrderMapper.selectOrderIdsByOwnerId(ownerId);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.selectByIdsAndState(ids, (byte) 0);
    }


    /**
     * 获取
     *
     * @return
     */
    @Override
    public List<RankParam> getOwnerRank() {

        // 先查出所有车主
        List<User> owners = userMapper.selectAllOwners();

        if (owners.size() == 0) {
            return null;
        }

        Map<Double, RankParam> map = new HashMap<>();
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
                map.put(result, rankParam);
            }
        }

        List<Map.Entry<Double, RankParam>> entries = new ArrayList<>(map.entrySet());
        entries.sort((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()));
        List<RankParam> rank = new ArrayList<>(owners.size());
        for (Map.Entry<Double, RankParam> entry : entries) {
            rank.add(entry.getValue());
        }
        return rank;
    }

    /**
     * 查询满足条件的订单
     *
     * @return
     */
    @Override
    public List<RankParam> searchOwnerOrders(String mobile) {
        List<RankParam> rank = getOwnerRank();
        if (mobile == null || mobile.trim().length() == 0) {
            return rank;
        }
        List<RankParam> result = new ArrayList<>();
        for (RankParam rankParam : rank) {
            String m = rankParam.getUser().getMobile();
            if (m.equals(mobile)) {
                result.add(rankParam);
                break;
            }
        }
        return result;
    }

    public User getCurrentUser() {
        return redisUtil.getCurrentUser("cur");
    }
}

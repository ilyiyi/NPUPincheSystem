package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.UserOrder;
import java.util.List;

public interface UserOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOrder row);

    UserOrder selectByPrimaryKey(Long id);

    List<UserOrder> selectAll();

    int updateByPrimaryKey(UserOrder row);

    List<Long> getOrderIDsByUserId(Long userId);
}
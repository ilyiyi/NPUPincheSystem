package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.UserOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOrder row);

    UserOrder selectByPrimaryKey(Long id);

    List<UserOrder> selectAll();

    int updateByPrimaryKey(UserOrder row);

    List<Long> getOrderIDsByUserId(Long userId);
}
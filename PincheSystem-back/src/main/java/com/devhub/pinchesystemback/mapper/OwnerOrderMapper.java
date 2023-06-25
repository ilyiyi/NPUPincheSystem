package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.OwnerOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OwnerOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OwnerOrder row);

    OwnerOrder selectByPrimaryKey(Long id);

    List<OwnerOrder> selectAll();

    int updateByPrimaryKey(OwnerOrder row);

    List<Long> selectOrderIdsByOwnerId(Long ownerId);
}
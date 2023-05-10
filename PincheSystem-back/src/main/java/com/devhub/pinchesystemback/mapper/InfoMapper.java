package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.Info;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InfoMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Info row);

    Info selectByPrimaryKey(Long infoId);

    List<Info> selectAll();

    List<Info> selectAllByOwnerId(Long ownerId);

    List<Info> selectByOwner(Long ownerId);

    int updateByPrimaryKey(Info row);

}
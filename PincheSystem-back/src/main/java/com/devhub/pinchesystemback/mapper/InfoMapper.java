package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    List<Info> selectInfoList(@Param("begin") Date begin, @Param("end") Date end, @Param("ownerId") Long id);

    List<Long> selectOwnerIdsByInfo();

    List<Long> selectInfoIdsByTime(@Param("begin") Date begin, @Param("end") Date end, @Param("ownerId") Long ownerId);

}
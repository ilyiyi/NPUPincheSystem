package com.devhub.pinchesystemback.mapper;


import com.devhub.pinchesystemback.domain.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Log row);

    Log selectByPrimaryKey(Long id);

    List<Log> selectAll();

    int updateByPrimaryKey(Log row);
}
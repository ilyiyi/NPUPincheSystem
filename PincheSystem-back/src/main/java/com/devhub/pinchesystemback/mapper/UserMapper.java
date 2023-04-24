package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User row);

    User selectByPrimaryKey(Long id);

    User selectByUsername(String username);

    List<User> selectAll();

    int updateByPrimaryKey(User row);
}
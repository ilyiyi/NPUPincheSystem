package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.User;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User row);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User row);
}
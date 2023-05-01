package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(String username, String password, String mobile);

    User selectByPrimaryKey(Long id);

    UserVO selectVOById(Long id);

    User selectByUsername(String username);

    List<User> selectAll();

    int updateByPrimaryKey(Long id,String username, String password, String mobile,String sex);
}
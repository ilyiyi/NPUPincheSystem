package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.mapper.UserMapper;
import com.devhub.pinchesystemback.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     */
    @Override
    public boolean register(String username, String password, String mobile) {
        if(username.length() == 0 || password.length() < 6) {
            return false;
        }
        User user1 = userMapper.selectByUsername(username);
        if(user1 == null){
            userMapper.insert(username,password,mobile);
            return true;
        }
        return false;
    }

    /**
     * 用户登录(如果用户名和密码匹配,会返回用户信息)
     *
     * @param username
     * @param password
     */
    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 根据userId获取用户的信息
     *
     * @param userId
     */
    @Override
    public User getInfo(Long userId) {
        return null;
    }

    /**
     * 根据userId,修改指定用户的信息(昵称和性别)
     *
     * @param userId
     * @param username
     * @param sex
     * @param department
     */
    @Override
    public void modifyInfo(Long userId, String username, Integer sex, String department) {

    }
}

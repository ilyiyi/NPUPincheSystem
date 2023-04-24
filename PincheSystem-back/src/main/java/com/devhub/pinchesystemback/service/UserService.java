package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.User;

/**
 * @author awater
 */
public interface UserService {
    /**
     * 用户注册
     */
    boolean register(String username, String password , String mobile);

    /**
     * 用户登录(如果用户名和密码匹配,会返回用户信息)
     */
    User login(String username, String password);

    /**
     * 根据userId获取用户的信息
     */
    User getInfo(Long userId);

    /**
     * 根据userId,修改指定用户的信息(昵称和性别)
     */
    void modifyInfo(Long userId, String username, Integer sex,String department);
}

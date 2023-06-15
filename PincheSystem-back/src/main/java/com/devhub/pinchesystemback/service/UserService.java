package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

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
    User login(String username, String password, HttpServletRequest request);

    /**
     * 根据userId获取用户的信息
     */
    UserVO getInfo(Long userId);

    void modifyInfo(Long userId, String username, String password, String mobile, String sex);

    boolean isDeleted(Long userId);

    boolean userLogout(HttpServletRequest request);
}

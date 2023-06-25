package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.pararm.ModifyParam;
import com.devhub.pinchesystemback.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author awater
 */
public interface UserService {
    /**
     * 用户注册
     */
    boolean register(String username, String password, String mobile,Byte role);

    /**
     * 用户登录(如果用户名和密码匹配,会返回用户信息)
     */
    User login(String username, String password, HttpServletRequest request);

    /**
     * 根据userId获取用户的信息
     */
    UserVO getInfo(Long userId);

    boolean modifyInfo(Long userId, ModifyParam userParam);

    boolean isDeleted(Long userId);

    boolean userLogout(HttpServletRequest request);

    /**
     * 根据ownerId获取积分
     * @param ownerId 车主id
     * @return
     */
    int getOwnerScore(Long ownerId);
}

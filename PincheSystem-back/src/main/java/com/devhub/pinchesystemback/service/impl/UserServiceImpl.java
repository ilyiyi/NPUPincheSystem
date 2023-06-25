package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.advice.annotation.Idempotent;
import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.exception.NotFoundException;
import com.devhub.pinchesystemback.mapper.UserMapper;
import com.devhub.pinchesystemback.pararm.ModifyParam;
import com.devhub.pinchesystemback.service.UserService;
import com.devhub.pinchesystemback.utils.RedisUtil;
import com.devhub.pinchesystemback.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     */
    @Override
    public boolean register(String username, String password, String mobile,Byte role) {
        if (username.length() == 0 || password.length() < 6) {
            return false;
        }
        User user1 = userMapper.selectByUsername(username);
        if (user1 == null) {
            userMapper.insert(username, passwordEncoder.encode(password), mobile,role);
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
    @Idempotent
    public User login(String username, String password, HttpServletRequest request) {
        User user = userMapper.selectByUsername(username);
        if (user == null || user.getPassword().equals(passwordEncoder.encode(password))) {
            throw new BusinessException(ResultCodeEnum.WRONG_USERNAME_OR_PASSWORD, "用户不存在");
        }
        User safeUser = getSafeUser(user);
        request.getSession().setAttribute("loginUser", safeUser);
        return safeUser;

    }

    public User getSafeUser(User user) {
        user.setPassword(null);
        return user;
    }

    /**
     * 根据userId获取用户的信息
     *
     * @param userId
     */
    @Override
    public UserVO getInfo(Long userId) {
        return userMapper.selectVOById(userId);
    }

    /**
     * 根据userId,修改指定用户的信息(昵称和性别)
     *
     * @param userParam 用户信息
     */
    @Override
    public boolean modifyInfo(Long userId, ModifyParam userParam){
        try {
            int match = userMapper.updateByPrimaryKey(userId, userParam.getUsername(), userParam.getSex(), userParam.getMobile());
            if (match == 0) {
                throw new NotFoundException("userId为" + userId + "的用户不存在");
            }
            return true;
        }catch (DataAccessException e){
            throw new RuntimeException("Bad SqlException");
        }
    }

    /**
     * @param userId 用户id
     * @return 是否被删除
     */
    @Override
    public boolean isDeleted(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user == null || user.getIsDelete() == 1;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUser");
        return true;
    }
}

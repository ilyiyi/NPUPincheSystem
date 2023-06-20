package com.devhub.pinchesystemback.config;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 这步就是根据输入Controller层输入地username去数据库拿出加密之后的password，准确地说，它是把整个用户都拿出来了，封装成UserDetails
     * 至于Controller层输入的密码是否和数据库里保存地密码一样，这个判断是由SpringSecurity帮我们做的
     * 它会把Controller层输入的密码按照PasswordEncoder进行加密，然后去和数据库里的加密后的密码判断是否匹配
     * 如果不匹配就会抛异常。（抛异常也没事，我已经在UserServiceImpl的login方法里面用try-catch捕获了）
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userMapper.selectByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return u;
    }
}

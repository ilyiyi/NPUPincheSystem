package com.devhub.pinchesystemback.utils;

import com.devhub.pinchesystemback.domain.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author wak
 * Redis 工具类
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, String> template;

    public boolean setValue(String key, String value) {
        template.opsForValue().set(key, value);
        return true;
    }

    public String get(String key) {
        return template.opsForValue().get(key);
    }

    public User getCurrentUser(String key) {
        String id = (String) template.opsForHash().get(key, "userId");
        String username = (String) template.opsForHash().get(key, "username");
        String mobile = (String) template.opsForHash().get(key, "mobile");
        String sex = (String) template.opsForHash().get(key, "sex");
        String role = (String) template.opsForHash().get(key, "role");


        User user = new User();
        user.setId(Long.valueOf(id));
        user.setRole(Byte.valueOf(role));
        user.setUsername(username);
        user.setMobile(mobile);
        user.setSex(sex);

        return user;
    }


    public boolean setObject(String key, User value) {
        template.opsForHash().put(key, "userId", value.getId().toString());
        template.opsForHash().put(key, "role", value.getRole().toString());
        template.opsForHash().put(key, "username", value.getUsername());
        template.opsForHash().put(key, "mobile", value.getMobile());

        template.opsForHash().put(key, "sex", value.getSex());

        return true;
    }

    public boolean setExpire(String key, Long expire) {
        try {
            if (expire > 0) {
                template.expire(key, expire, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将set加入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
//    public boolean sSet(String key, Object... value) {
//        try {
//            template.opsForSet().add(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}

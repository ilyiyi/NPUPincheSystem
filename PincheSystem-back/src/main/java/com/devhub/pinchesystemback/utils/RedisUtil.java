package com.devhub.pinchesystemback.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wak
 * Redis 工具类
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> template;

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
    public boolean sSet(String key, Object... value) {
        try {
            template.opsForSet().add(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sSet(String key, Long expire, Object... value) {
        try {
            template.opsForSet().add(key, value);
            if (expire > 0) {
                setExpire(key, expire);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long sSize(String key) {
        try {
            if (Boolean.TRUE.equals(template.hasKey(key))) {
                return template.opsForSet().size(key);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Set<Object> sGet(String key) {
        try {
            return template.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

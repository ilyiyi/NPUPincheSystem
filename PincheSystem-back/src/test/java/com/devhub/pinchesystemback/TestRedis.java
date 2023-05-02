package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wak
 */
@SpringBootTest
public class TestRedis {

    @Resource
    private RedisTemplate<String, Object> template;

    @Resource
    private RedisUtil redisUtil;


    @Test
    public void testPush() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        redisUtil.sSet("user:1", set.toArray(new Integer[0]));
    }

}

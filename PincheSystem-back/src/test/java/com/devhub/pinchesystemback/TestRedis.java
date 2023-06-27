package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author wak
 */
@SpringBootTest
public class TestRedis {


    @Resource
    private RedisTemplate<String, String> template;

    @Resource
    private RedisUtil redisUtil;


    @Test
    public void testSerializeUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("wak");
        user.setMobile("15023546975");
        user.setSex("男");
        user.setRole((byte) 0);

    }

    @Test
    public void testCurrentUser() {
//        String id = (String) template.opsForHash().get("cur", "userId");
//        String username = (String) template.opsForHash().get("cur", "username");
//        String mobile = (String) template.opsForHash().get("cur", "mobile");
//        String sex = (String) template.opsForHash().get("cur", "sex");
//        String role = (String) template.opsForHash().get("cur", "role");
//
//        System.out.println(Long.parseLong(id));
//        System.out.println(username);
//        System.out.println(mobile);
//        System.out.println(sex);
//        System.out.println(Byte.parseByte(role));
//        System.out.println(role);
        User cur = redisUtil.getCurrentUser("cur");
        System.out.println(cur);
    }

    @Test
    public void testFormat(){
        String x = "0";
        Byte b = 0;
        System.out.println(b.toString());
        System.out.println(x);
        System.out.println(Integer.parseInt(x));
    }
}

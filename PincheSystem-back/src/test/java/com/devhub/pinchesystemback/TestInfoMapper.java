package com.devhub.pinchesystemback;

import com.devhub.pinchesystemback.mapper.InfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TestInfoMapper {

    @Resource
    private InfoMapper mapper;


    @Test
    public void testSelectAll() {
        mapper.selectAll().forEach(System.out::println);
    }
}

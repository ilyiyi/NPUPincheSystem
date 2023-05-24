package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.mapper.InfoMapper;
import com.devhub.pinchesystemback.mapper.UserMapper;
import com.devhub.pinchesystemback.service.AdminService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wak
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private InfoMapper mapper;

    @Resource
    private RedisTemplate<String, String> template;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询一段时间内所有拼车订单或者某位车主的所有拼车订单
     *
     * @param begin   起始时间
     * @param end     截止时间
     * @param ownerId 车主id
     * @return 拼车信息列表
     */
    @Override
    public List<Info> getAllInfosByTime(Date begin, Date end, Long ownerId) {
        return mapper.selectInfoList(begin, end, ownerId);
    }

    /**
     * 生成一段时间内的车主排名
     *
     * @param begin 起始时间
     * @param end   截止时间
     * @return 车主排名列表
     */
    @Override
    public List<User> getOwnerRank(Date begin, Date end) {
        List<Info> infos = mapper.selectAll();

        String key = "rank";
        for (Info info : infos) {
            String id = String.valueOf(info.getOwnerId());
            Double score = template.opsForZSet().score(key, id);
            if (score != null) {
                template.opsForZSet().add(key, id, score + 1);
            } else {
                template.opsForZSet().add(key, id, 1);
            }
        }

        Set<String> ids = template.opsForZSet().range(key, 0, -1);

        List<User> rankList = new ArrayList<>();

        if (ids != null) {
            for (String id : ids) {
                User user = userMapper.selectByPrimaryKey(Long.valueOf(id));
                rankList.add(user);
            }
        }

        return rankList;
    }
}

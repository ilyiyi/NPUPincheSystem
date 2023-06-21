package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.mapper.InfoMapper;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import com.devhub.pinchesystemback.service.InfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 发布拼车信息
     *
     * @return 是否成功
     */
    @Override
    public boolean publish(Info info) {
        infoMapper.insert(info);
        return info.getInfoId() != null;
    }

    /**
     * 撤销拼车信息
     *
     * @return 是否成功
     */
    @Override
    public boolean cancel(Long infoId) {
        List<Long> orderIds = orderMapper.getOrderByInfoId(infoId);
        orderMapper.deleteByOrderIds(orderIds);
        infoMapper.deleteByPrimaryKey(infoId);
        return infoId == null;
    }

    /**
     * 修改拼车信息
     *
     * @return 是否成功
     */
    @Override
    public boolean modify(Info info) {
        if (infoMapper.selectByPrimaryKey(info.getInfoId()) != null) {
            infoMapper.updateByPrimaryKey(info);
            return true;
        }
        return false;
    }

    /**
     * 获取拼车信息
     *
     * @param infoId 拼车信息Id
     * @return 拼车信息
     */
    @Override
    public Info getInfo(Long infoId) {
        Info info = infoMapper.selectByPrimaryKey(infoId);
        System.out.println(info.toString());
        return info;
    }

    /**
     * 获取所有拼车信息
     *
     * @return 所有拼车信息
     */
    @Override
    public List<Info> getInfos(Long ownerId) {
        return infoMapper.selectByOwner(ownerId);
    }

    /**
     * 分页返回所有拼车信息
     *
     * @return 所有拼车信息
     */
    public PageInfo<Info> getInfos(Long ownerId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Info> infos = infoMapper.selectAllByOwnerId(ownerId);
        return new PageInfo<>(infos);
    }

    @Override
    public List<Info> getAllInfos() {
        return infoMapper.selectAll();
    }
}

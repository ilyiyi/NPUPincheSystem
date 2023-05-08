package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.Info;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface InfoService {

    /**
     * 发布拼车信息
     * @return 是否成功
     */
    boolean publish(Info info);

    /**
     * 撤销拼车信息
     * @return 是否成功
     */
    boolean cancel(Long infoId);

    /**
     * 修改拼车信息
     * @return 是否成功
     */
    boolean modify(Info info);

    /**
     * 获取拼车信息
     * @param infoId 拼车信息Id
     * @return 拼车信息
     */
    Info getInfo(Long infoId);

    /**
     * 获取所有拼车信息
     * @return 所有拼车信息
     */
    List<Info> getInfos(Long ownerId);

    /**
     * 分页返回所有拼车信息
     *
     * @return 所有拼车信息
     */
    PageInfo<Info> getInfos(int currentPage, int pageSize);
}

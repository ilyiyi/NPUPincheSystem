package com.devhub.pinchesystemback.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Info {
    /**
     * 拼车信息id
     */
    private Long infoId;

    /**
     * 发布者id
     */
    private Long ownerId;

    /**
     * 发布者姓名
     */
    private String ownerName;

    /**
     * 发车日期
     */
    private Date days;

    /**
     * 目的地
     */
    private String ending;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 价格
     */
    private Float price;

    /**
     * 座位余量
     */
    private Byte remain;

    /**
     * 备注
     */
    private String remark;

}
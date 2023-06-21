package com.devhub.pinchesystemback.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
package com.devhub.pinchesystemback.pararm;

import lombok.Data;

import java.util.Date;

/**
 * 拼车信息参数
 */
@Data
public class InfoParam {
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

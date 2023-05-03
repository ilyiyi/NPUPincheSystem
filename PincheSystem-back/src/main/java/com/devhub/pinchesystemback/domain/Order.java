package com.devhub.pinchesystemback.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author wak
 */
@Data
public class Order {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 乘客数量
     */
    private Byte passengerNum;
    /**
     * 终点站
     */
    private String ending;
    /**
     * 拼车信息id
     */
    private Long infoId;
    /**
     * 发车时间
     */
    private Date startTime;
    /**
     * 价格
     */
    private Double price;
    /**
     * 订单状态
     */
    private Byte orderState;
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

package com.devhub.pinchesystemback.pararm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class OrderParam {

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    private Long ownerId;
}

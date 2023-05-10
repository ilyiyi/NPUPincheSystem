package com.devhub.pinchesystemback.pararm;

import lombok.Data;

/**
 * 订单审核参数
 */
@Data
public class OrderReviewParam {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单状态
     */
    private Byte orderState;
}

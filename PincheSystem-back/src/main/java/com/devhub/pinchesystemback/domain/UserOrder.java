package com.devhub.pinchesystemback.domain;

import lombok.Data;

@Data
public class UserOrder {
    private Long id;

    private Long orderId;

    private Long userId;
}
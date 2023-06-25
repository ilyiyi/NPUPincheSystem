package com.devhub.pinchesystemback.pararm;

import com.devhub.pinchesystemback.domain.User;
import lombok.Data;

@Data
public class RankParam {
    private User user;
    private double successRate;

    private int count;
}

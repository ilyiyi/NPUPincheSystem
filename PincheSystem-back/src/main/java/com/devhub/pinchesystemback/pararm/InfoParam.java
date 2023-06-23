package com.devhub.pinchesystemback.pararm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 拼车信息参数
 */
@Data
@NoArgsConstructor
public class InfoParam {
    /**
     * 发布者姓名
     */
    private String ownerName;

    /**
     * 发车日期
     */
    private String days;

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
    @NonNull
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

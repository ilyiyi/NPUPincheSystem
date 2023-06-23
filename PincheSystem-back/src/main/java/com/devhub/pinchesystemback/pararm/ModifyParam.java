package com.devhub.pinchesystemback.pararm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 修改信息参数
 */
@Data
public class ModifyParam {
    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户电话号码
     */
    private String mobile;
}

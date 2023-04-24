package com.devhub.pinchesystemback.pararm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 注册参数
 */
@Data
public class RegisterParam {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度最大为20")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6到20")
    private String password;

    /**
     *用户电话号码
     */
    private String mobile;

}

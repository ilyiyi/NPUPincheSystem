package com.devhub.pinchesystemback.domain;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String mobile;

    private String sex;

    private Byte role;

    private Date createTime;

    private Date updateTime;

    private Byte isDelete;

}
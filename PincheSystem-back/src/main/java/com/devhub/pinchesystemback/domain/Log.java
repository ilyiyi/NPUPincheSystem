package com.devhub.pinchesystemback.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Log {
    private Long id;

    private String url;

    private String type;

    private String method;

    private String args;

    private Float totalTime;

    private String operateUser;

    private Date time;
}
package com.devhub.pinchesystemback.pararm;

import lombok.Data;

import java.util.Date;
@Data
public class SearchParam {
    private Date begin;
    private Date end;
    private String mobile;
}

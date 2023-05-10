package com.devhub.pinchesystemback.pararm;

import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageParam {
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 页面大小
     */
    private int pageSize;
}

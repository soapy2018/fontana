package com.fontana.base.query;

import lombok.Getter;

/**
 * @className: PageParam
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/3/16 13:49
 */
@Getter
public class PageParam {

    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_MAX_SIZE = 100;

    /**
     * 分页号码，从1开始计数。
     */
    private Integer pageNum = DEFAULT_PAGE_NUM;

    /**
     * 每页大小。
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 设置当前分页页号。
     *
     * @param pageNum 页号，如果传入非法值，则使用缺省值。
     */
    public void setPageNum(Integer pageNum) {
        if (pageNum == null) {
            return;
        }
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        this.pageNum = pageNum;
    }

    /**
     * 设置分页的大小。
     *
     * @param pageSize 分页大小，如果传入非法值，则使用缺省值。
     */
    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            return;
        }
        if (pageSize <= 0 || pageSize > DEFAULT_MAX_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }
}



package com.fontana.base.vo;

import lombok.Data;

import java.util.Date;

/**
 * @className: BaseVO
 * @Description: 基础VO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/3/16 16:24
 */
@Data
public abstract class BaseVO {

    /**
     * 创建者Id。
     */
    private Long createUserId;

    /**
     * 创建者显示名称。
     */
    private String createUserName;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 更新者Id。
     */
    private Long updateUserId;

    /**
     * 更新者显示名称。
     */
    private String updateUserName;

    /**
     * 更新时间。
     */
    private Date updateTime;
}



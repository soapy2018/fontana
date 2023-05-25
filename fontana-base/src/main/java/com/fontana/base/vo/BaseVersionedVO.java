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
public abstract class BaseVersionedVO extends  BaseVO{

    /**
     * 主键。
     */
    private Long id;
    /**
     * 版本。
     */
    private Integer version;

    /**
     * 是否挂起状态。（0-正常 1-挂起）
     */
    private Boolean pendingFlag;

    /**
     * 是否激活状态。（0-未激活 1-激活）
     */
    private Boolean activeFlag;

}



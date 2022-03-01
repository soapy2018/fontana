package com.bluetron.nb.common.upmsservice.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 映射超类。提供了持久化实体状态（即属性或字段）和映射信息，但它本身不是一个实体。
 * 通常情况下，这种超类映射的的目的是定义多个实体共有的状态和映射信息。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@MappedSuperclass
public abstract class JpaBaseEntity {

    /**
     * 创建者Id。
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间。
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者Id。
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 更新时间。
     */
    @Column(name = "update_time")
    private Date updateTime;
}

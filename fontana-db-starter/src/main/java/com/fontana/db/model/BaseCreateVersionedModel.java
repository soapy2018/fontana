package com.fontana.db.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体对象的公共基类，所有子类均必须包含基类定义的数据表字段和实体对象字段。
 * 用于有版本信息的实体对象，它没有更新记录，只有创建新的版本
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCreateVersionedModel extends BaseCreateModel {

    /**
     * 版本。
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 是否激活状态。
     */
    @TableField(value = "active_status")
    private Boolean activeStatus;

    @Override
    public String toString() {
        return super.toString();
    }
}

package com.fontana.db.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带逻辑删除 数据表实体
 *
 * @author CQF
 * @since 2021/12/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseVersionedModel extends BaseLogicDelModel{


    /**
     * 主键。
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 版本。
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 是否挂起状态。（0-正常 1-挂起）
     */
    @TableField(value = "pending_flag")
    private Boolean pendingFlag;

    /**
     * 是否激活状态。（0-未激活 1-激活）
     */
    @TableField(value = "active_flag")
    private Boolean activeFlag;

    @Override
    public String toString() {
        return super.toString();
    }
}



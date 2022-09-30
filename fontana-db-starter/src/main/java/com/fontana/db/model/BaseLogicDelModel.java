package com.fontana.db.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
public abstract class BaseLogicDelModel extends BaseModel {

    /**
     * 逻辑删除标记字段(0: 正常 1: 已删除), 新增时通过字段在数据库定义默认值(0)来设置
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    @Override
    public String toString() {
        return super.toString();
    }
}



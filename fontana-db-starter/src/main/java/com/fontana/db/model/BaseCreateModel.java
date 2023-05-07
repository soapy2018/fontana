package com.fontana.db.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 实体对象的公共基类，所有子类均必须包含基类定义的数据表字段和实体对象字段。
 * 用于有版本信息的实体对象，它没有更新记录，只有创建新的版本
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCreateModel extends BaseJsonModel {

    /**
     * 创建者Id。
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 创建者名称，是显示名，不是登录名。
     */
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Override
    public String toString() {
        return super.toString();
    }
}

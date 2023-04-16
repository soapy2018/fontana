package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据权限与用户关联实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@TableName(value = "bn_sys_data_perm_user")
public class SysDataPermUser {

    /**
     * 数据权限Id。
     */
    @TableField(value = "data_perm_id")
    private Long dataPermId;

    /**
     * 用户Id。
     */
    @TableField(value = "user_id")
    private Long userId;
}

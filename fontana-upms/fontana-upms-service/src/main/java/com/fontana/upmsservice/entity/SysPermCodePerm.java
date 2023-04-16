package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限字与权限资源关联实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@TableName(value = "bn_sys_perm_code_perm")
public class SysPermCodePerm {

    /**
     * 权限字Id。
     */
    @TableField(value = "perm_code_id")
    private Long permCodeId;

    /**
     * 权限Id。
     */
    @TableField(value = "perm_id")
    private Long permId;
}

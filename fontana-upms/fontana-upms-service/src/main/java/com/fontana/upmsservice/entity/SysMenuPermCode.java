package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 菜单与权限字关联实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@TableName(value = "bn_sys_menu_perm_code")
public class SysMenuPermCode {

    /**
     * 关联菜单Id。
     */
    @TableField(value = "menu_id")
    private Long menuId;

    /**
     * 关联权限字Id。
     */
    @TableField(value = "perm_code_id")
    private Long permCodeId;
}

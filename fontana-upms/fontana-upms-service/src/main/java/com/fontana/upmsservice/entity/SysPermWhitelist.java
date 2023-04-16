package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 白名单实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@TableName(value = "bn_sys_perm_whitelist")
public class SysPermWhitelist {

    /**
     * 权限资源的URL。
     */
    @TableId(value = "perm_url")
    private String permUrl;

    /**
     * 权限资源所属模块名字(通常是Controller的名字)。
     */
    @TableField(value = "module_name")
    private String moduleName;

    /**
     * 权限的名称。
     */
    @TableField(value = "perm_name")
    private String permName;
}

package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.db.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 权限模块实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bn_sys_perm_module")
public class SysPermModule extends BaseModel {

    /**
     * 权限模块Id。
     */
    @TableId(value = "module_id")
    private Long moduleId;

    /**
     * 上级权限模块Id。
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 权限模块名称。
     */
    @TableField(value = "module_name")
    private String moduleName;

    /**
     * 权限模块类型(0: 普通模块 1: Controller模块)。
     */
    @TableField(value = "module_type")
    private Integer moduleType;

    /**
     * 权限模块在当前层级下的顺序，由小到大。
     */
    @TableField(value = "show_order")
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    @TableField(exist = false)
    private List<SysPerm> sysPermList;
}

package com.bluetron.nb.common.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bluetron.nb.common.base.annotation.RelationDict;
import com.bluetron.nb.common.db.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Map;

/**
 * 权限资源实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bn_sys_perm")
public class SysPerm extends BaseModel {

    /**
     * 权限Id。
     */
    @TableId(value = "perm_id")
    private Long permId;

    /**
     * 权限所在的权限模块Id。
     */
    @TableField(value = "module_id")
    private Long moduleId;

    /**
     * 权限名称。
     */
    @TableField(value = "perm_name")
    private String permName;

    /**
     * 关联的URL。
     */
    private String url;

    /**
     * 权限在当前模块下的顺序，由小到大。
     */
    @TableField(value = "show_order")
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    @RelationDict(
            masterIdField = "moduleId",
            slaveServiceName = "SysPermModuleService",
            slaveModelClass = SysPermModule.class,
            slaveIdField = "moduleId",
            slaveNameField = "moduleName")
    @TableField(exist = false)
    private Map<String, Object> moduleIdDictMap;
}

package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * 数据权限与部门关联实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@ToString(of = {"deptId"})
@TableName(value = "bn_sys_data_perm_dept")
public class SysDataPermDept {

    /**
     * 数据权限Id。
     */
    @TableField(value = "data_perm_id")
    private Long dataPermId;

    /**
     * 关联部门Id。
     */
    @TableField(value = "dept_id")
    private Long deptId;
}

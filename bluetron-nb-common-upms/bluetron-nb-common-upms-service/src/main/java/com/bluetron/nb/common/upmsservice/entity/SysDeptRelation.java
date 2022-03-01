package com.bluetron.nb.common.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门关联实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "bn_sys_dept_relation")
public class SysDeptRelation {

    /**
     * 上级部门Id。
     */
    @TableField(value = "parent_dept_id")
    private Long parentDeptId;

    /**
     * 部门Id。
     */
    @TableField(value = "dept_id")
    private Long deptId;
}

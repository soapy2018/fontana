package com.bluetron.nb.common.upmsapi.vo;

import lombok.Data;

/**
 * 数据权限与部门关联VO。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
public class SysDataPermDeptVo {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 关联部门Id。
     */
    private Long deptId;
}
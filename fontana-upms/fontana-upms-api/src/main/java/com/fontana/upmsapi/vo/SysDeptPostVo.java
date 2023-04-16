package com.fontana.upmsapi.vo;

import lombok.Data;

/**
 * 部门岗位VO对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
public class SysDeptPostVo {

    /**
     * 部门岗位Id。
     */
    private Long deptPostId;

    /**
     * 部门Id。
     */
    private Long deptId;

    /**
     * 岗位Id。
     */
    private Long postId;

    /**
     * 部门岗位显示名称。
     */
    private String postShowName;
}

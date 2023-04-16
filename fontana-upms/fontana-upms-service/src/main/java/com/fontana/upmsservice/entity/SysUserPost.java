package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户岗位多对多关系实体对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@TableName(value = "bn_sys_user_post")
public class SysUserPost {

    /**
     * 用户Id。
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 部门岗位Id。
     */
    @TableField(value = "dept_post_id")
    private Long deptPostId;

    /**
     * 岗位Id。
     */
    @TableField(value = "post_id")
    private Long postId;
}

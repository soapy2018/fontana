package com.fontana.upmsapi.dto;

import com.fontana.base.validate.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 部门岗位Dto对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
public class SysDeptPostDto {

    /**
     * 部门岗位Id。
     */
    @NotNull(message = "数据验证失败，部门岗位Id不能为空！", groups = {UpdateGroup.class})
    private Long deptPostId;

    /**
     * 部门Id。
     */
    @NotNull(message = "数据验证失败，部门Id不能为空！", groups = {UpdateGroup.class})
    private Long deptId;

    /**
     * 岗位Id。
     */
    @NotNull(message = "数据验证失败，岗位Id不能为空！", groups = {UpdateGroup.class})
    private Long postId;

    /**
     * 部门岗位显示名称。
     */
    @NotBlank(message = "数据验证失败，部门岗位显示名称不能为空！")
    private String postShowName;
}
